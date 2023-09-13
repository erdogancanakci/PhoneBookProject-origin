package com.example.application.view;

import com.example.application.data.PersonDataProvider;
import com.example.application.manager.DBPersonManager;
import com.example.application.repository.PersonDataRepository;
import com.example.application.data.Person;
import com.example.application.personutil.PersonService;
import com.example.application.manager.PersonManager;
import com.vaadin.flow.component.crud.*;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;

import java.util.*;
import java.util.List;

import static com.example.application.repository.PersonDataRepository.getIdToPhoneMap;

@Route("")
public class PhoneBookView extends Div {
    private final Crud<Person> crud;
    private Grid<Person> grid;
    private TextField nameFilter, lastNameFilter, emailFilter;
    private boolean isEditMode = false;
    DBPersonManager dbPersonManager = new DBPersonManager();
    PersonManager personManager = new PersonManager();

    public PhoneBookView() {
        grid = new Grid<>(Person.class );
        crud = new Crud<>(Person.class, grid, createEditor());

        setupGrid();
        Crud.addEditColumn(grid);
        addListener();
        prepareFilterFields();
        add(crud);
        PersonDataProvider.getPersonDataProvider(); //it is needed if it is the first time the running the application on your local
        dbPersonManager.readPersonTable();
    }

    private void addListener () {

        crud.addCancelListener(e -> isEditMode = false);
        crud.addEditListener(e -> isEditMode = true);

        crud.addSaveListener(e -> {
            if (isEditMode) {
                personManager.updatePersonInPhonebook(e.getItem());
            }
            else {
                personManager.addPersonToPhonebook(e.getItem());
            }
            isEditMode = false;
        });

        crud.addDeleteListener(e -> {
            int oldNumber = getIdToPhoneMap().get(e.getItem().getId());
            if(e.getItem().getPhoneNumber() == oldNumber) {
                personManager.removePersonFromPhonebook(e.getItem());
            } else {
                e.getItem().setPhoneNumber(oldNumber);
                personManager.removePersonFromPhonebook(e.getItem());
            }
            isEditMode = false;
        });
    }

    private CrudEditor<Person> createEditor() {
        final TextField name = new TextField("Name");
        final TextField lastName = new TextField("LastName");
        final TextField street = new TextField("Street");
        final TextField city = new TextField("City");
        final TextField country = new TextField("Country");
        final IntegerField phoneNumber = new IntegerField("PhoneNumber");
        final TextField email = new TextField("Email");

        FormLayout form = new FormLayout(name, street, lastName, city, country, phoneNumber, email);

        Binder<Person> binder = new Binder<>(Person.class);
        binder.forField(name).asRequired("Name must be entered").bind(Person::getName,
                Person::setName);
        binder.forField(lastName).asRequired("Last name must be entered").bind(Person::getLastName,
                Person::setLastName);
        binder.forField(street).asRequired("Street must be entered").bind(Person::getStreet,
                Person::setStreet);
        binder.forField(city).asRequired("City must be entered").bind(Person::getCity,
                Person::setCity);
        binder.forField(country).asRequired("Country must be entered").bind(Person::getCountry,
                Person::setCountry);
        binder.forField(phoneNumber).asRequired().bind(Person::getPhoneNumber,
                Person::setPhoneNumber);
        binder.forField(email).asRequired("Email must be entered").bind(Person::getEmail,
                Person::setEmail);

        return new BinderCrudEditor<>(binder, form);
    }

    private void setupGrid() {

        crud.setDataProvider(DataProvider.ofCollection(PersonDataRepository.getIdToPersonMap().values()));

        grid = crud.getGrid();
        grid.addItemDoubleClickListener(event -> crud.edit(event.getItem(),
                Crud.EditMode.EXISTING_ITEM));

        setupGridColumns();
        setupLazyLoading();
    }

    private void setupLazyLoading() {
        DataProvider<Person, Void> dataProvider =
                DataProvider.fromCallbacks(
                        query -> {
                            int offset = query.getOffset();
                            int limit = query.getLimit();
                            List<Person> allPersons = new ArrayList<>(PersonDataRepository.getIdToPersonMap().values()).subList(offset, offset + limit);
                            return allPersons.stream();
                        },
                        query -> PersonService.getPersonIDToPersonMapSize()
                );
        grid.setDataProvider(dataProvider);
    }

    private void setupGridColumns() {
        List<String> visibleColumns = Arrays.asList("name", "lastName", "email");

        grid.getColumns().forEach(column -> {
            String key = column.getKey();
            if (!visibleColumns.contains(key)) {
                grid.removeColumn(column);
            }
        });

        grid.setColumnOrder(grid.getColumnByKey("name"),
                grid.getColumnByKey("lastName"),
                grid.getColumnByKey("email"));
    }

    private void prepareFilterFields() {
        HeaderRow headerRow = grid.appendHeaderRow();

        nameFilter = gridTextFieldFilter("name", headerRow);
        lastNameFilter = gridTextFieldFilter("lastName", headerRow);
        emailFilter = gridTextFieldFilter("email", headerRow);
    }

    private TextField gridTextFieldFilter(String columnKey, HeaderRow headerRow) {
        TextField filter = new TextField();
        filter.setValueChangeMode(ValueChangeMode.TIMEOUT);
        filter.addValueChangeListener(event -> this.onFilterChange());
        filter.setWidthFull();
        headerRow.getCell(grid.getColumnByKey(columnKey)).setComponent(filter);
        return filter;
    }

    private void onFilterChange() {
        String nameFilterText = nameFilter.getValue().toLowerCase();
        String lastNameFilterText = lastNameFilter.getValue().toLowerCase();
        String emailFilterText = emailFilter.getValue().toLowerCase();

        CallbackDataProvider<Person, Void> dataProvider = DataProvider
                .fromFilteringCallbacks(
                        query -> {
                            List<Person> filteredPersons = PersonDataRepository.getIdToPersonMap().values().stream()
                                    .filter(person -> {
                                        boolean nameMatch = person.getName().toLowerCase().contains(nameFilterText);
                                        boolean lastNameMatch = person.getLastName().toLowerCase().contains(lastNameFilterText);
                                        boolean emailMatch = person.getEmail().toLowerCase().contains(emailFilterText);
                                        return nameMatch && lastNameMatch && emailMatch;
                                    })
                                    .skip(query.getOffset())
                                    .limit(query.getLimit())
                                    .toList();

                            return filteredPersons.stream();
                        },
                        query -> {
                            long totalCount = PersonDataRepository.getIdToPersonMap().values().stream()
                                    .filter(person -> {
                                        boolean nameMatch = person.getName().toLowerCase().contains(nameFilterText);
                                        boolean lastNameMatch = person.getLastName().toLowerCase().contains(lastNameFilterText);
                                        boolean emailMatch = person.getEmail().toLowerCase().contains(emailFilterText);
                                        return nameMatch && lastNameMatch && emailMatch;
                                    })
                                    .count();

                            return (int) totalCount;
                        });

        grid.setDataProvider(dataProvider);
    }

}