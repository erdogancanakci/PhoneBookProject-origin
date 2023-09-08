package com.example.application.view;

import com.example.application.storage.PersonDataStorage;
import com.example.application.data.Person;
import com.example.application.personutil.PersonService;
import com.example.application.data.PersonDataProvider;
import com.example.application.bookmanager.PhoneBookManager;
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

@Route("")
public class PhoneBookView extends Div {
    private final Crud<Person> crud;
    private Grid<Person> grid;
    private TextField nameFilter, lastNameFilter, emailFilter;
    private boolean isEditMode = false;

    public PhoneBookView() {
        grid = new Grid<>(Person.class );
        crud = new Crud<>(Person.class, grid, createEditor());
        setupGrid();
        Crud.addEditColumn(grid);
        crud.addEditListener(e -> {
            isEditMode = true;
        });
        crud.addSaveListener(e -> {
            if (isEditMode) {
                PhoneBookManager.updatePerson(e.getItem());
            }
            else {
                PhoneBookManager.addPerson(e.getItem());
            }
            isEditMode = false;
        });
        crud.addDeleteListener(e -> PhoneBookManager.removePerson(e.getItem()));

        prepareFilterFields();
        add(crud);
        PersonDataProvider.getPersonDataProvider();
        setHeightFull();
        setWidthFull();
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
        binder.forField(lastName).asRequired("last name must be entered").bind(Person::getLastName,
                Person::setLastName);
        binder.forField(street).asRequired("street must be entered").bind(Person::getStreet,
                Person::setStreet);
        binder.forField(city).asRequired("city must be entered").bind(Person::getCity,
                Person::setCity);
        binder.forField(country).asRequired("country must be entered").bind(Person::getCountry,
                Person::setCountry);
        binder.forField(phoneNumber).asRequired().bind(Person::getPhoneNumber,
                Person::setPhoneNumber);
        binder.forField(email).asRequired("email must be entered").bind(Person::getEmail,
                Person::setEmail);

        return new BinderCrudEditor<>(binder, form);
    }

    private void setupGrid() {

        crud.setDataProvider(DataProvider.ofCollection(PersonDataStorage.getIDtoPerson().values()));

        grid = crud.getGrid();
        grid.addItemDoubleClickListener(event -> crud.edit(event.getItem(),
                Crud.EditMode.EXISTING_ITEM));
        grid.setWidthFull();
        grid.setHeightFull();
        grid.setPageSize(100);

        List<String> visibleColumns = Arrays.asList("name", "lastName", "email");
        grid.getColumns().forEach(column -> {
            String key = column.getKey();
            if (!visibleColumns.contains(key)) {
                grid.removeColumn(column);
            }
        });
  

        DataProvider<Person, Void> dataProvider =
                DataProvider.fromCallbacks(
                        query -> {
                            int offset = query.getOffset();
                            int limit = query.getLimit();
                            List<Person> allPersons = new ArrayList<>(PersonDataStorage.getIDtoPerson().values()).subList(offset, offset + limit);
                            return allPersons.stream();
                        },
                        query -> PersonService.getPersonIDToPersonMapSize()
                );

        grid.setPageSize(6);
        grid.setDataProvider(dataProvider);

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
                            List<Person> filteredPersons = PersonDataStorage.getIDtoPerson().values().stream()
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
                            long totalCount = PersonDataStorage.getIDtoPerson().values().stream()
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