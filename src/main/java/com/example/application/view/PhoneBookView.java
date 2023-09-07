package com.example.application.view;

import com.example.application.book_manager.PersonDataStorage;
import com.example.application.person_information.Person;
import com.example.application.person_provider.PersonService;
import com.example.application.person_provider.RandomPersonGenerator;
import com.example.application.book_manager.PhoneBookManager;
import com.vaadin.flow.component.crud.*;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;

import java.util.*;

@Route("")
public class PhoneBookView extends Div {
    private Crud<Person> crud;
    private Grid<Person> grid;
    private TextField nameFilter, lastNameFilter, emailFilter;

    public PhoneBookView() {
        grid = new Grid<>(Person.class );
        crud = new Crud<>(Person.class, grid, createEditor());
        setupGrid();
        crud.addSaveListener(e -> PhoneBookManager.saveOrUpdatePerson(e.getItem()));
        crud.addDeleteListener(e -> PhoneBookManager.removePerson(e.getItem()));
        Crud.addEditColumn(grid);
        prepareFilterFields();
        add(crud);
        RandomPersonGenerator.getInstance();

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
        crud.setDataProvider(DataProvider.ofCollection(PersonDataStorage.getPersonMap().values()));
        grid = crud.getGrid();


        grid.addItemDoubleClickListener(event -> crud.edit(event.getItem(),
                Crud.EditMode.EXISTING_ITEM));

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

                            List<Person> persons = PersonService.getPersonAsSublist(offset, limit);

                            return persons.stream();
                        },
                        query -> PersonService.getCount()
                );

        grid.setPageSize(60);
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
        ListDataProvider<Person> listDataProvider = (ListDataProvider<Person>) grid.getDataProvider();

        listDataProvider.setFilter(person -> {
            boolean nameMatch = true;
            boolean lastNameFilterMatch = true;
            boolean emailFilterMatch = true;
            if(!nameFilter.isEmpty()){
                nameMatch = person.getName().toLowerCase().contains(nameFilter.getValue());
            }
            if(!lastNameFilter.isEmpty()){
                lastNameFilterMatch = person.getLastName().toLowerCase().contains(lastNameFilter.getValue());
            }
            if(!emailFilter.isEmpty()){
                emailFilterMatch = person.getEmail().toLowerCase().contains(emailFilter.getValue());
            }
            return nameMatch && lastNameFilterMatch && emailFilterMatch;
        });
    }
}