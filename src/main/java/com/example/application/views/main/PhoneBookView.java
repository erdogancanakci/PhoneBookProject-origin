package com.example.application.views.main;

import com.example.application.Person;
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

import java.sql.SQLException;
import java.util.*;
@Route("")
public class PhoneBookView extends Div {

    private Crud<Person> crud;
    private Grid<Person> grid;
    private final Map<Integer,Person> personMap = new HashMap<>(); //first field are corresponding to ID value
    private final Map<Integer, Integer> phoneNumberSet = new HashMap<Integer, Integer>(); //first field is id, second is PhoneNumber
    private TextField nameFilter, lastNameFilter, emailFilter;

    public PhoneBookView() throws SQLException {

        grid = new Grid<>(Person.class );
        crud = new Crud<>(Person.class, grid, createEditor());
        setupGrid();
        crud.addSaveListener(e -> addPerson(e.getItem()));
        crud.addDeleteListener(e -> deletePerson(e.getItem()));
        Crud.addEditColumn(grid);
        prepareFilterFields();
        add(crud);
    }

     private synchronized void addPerson(Person item) {
        if(!phoneNumberSet.containsKey(item.getId())) {
            if(!phoneNumberSet.containsValue(item.getPhoneNumber())) {
                personMap.put(item.getId(), item);
                phoneNumberSet.put(item.getId(), item.getPhoneNumber());
            }
        }
        else {
            int oldNumber = phoneNumberSet.get(item.getId());
            if(!phoneNumberSet.containsValue(item.getPhoneNumber())) {
                phoneNumberSet.replace(item.getId(), oldNumber, item.getPhoneNumber());
                item.setPhoneNumber(item.getPhoneNumber());
            }
            else {
                phoneNumberSet.replace(item.getId(), oldNumber, oldNumber);
                item.setPhoneNumber(oldNumber);
            }
        }
         grid.setDataProvider(grid.getDataProvider());
     }

     private synchronized void deletePerson(Person item) {
        personMap.remove(item.getId(), item);
        phoneNumberSet.remove(item.getId());
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
        binder.forField(name).asRequired().bind(Person::getName,
                Person::setName);
        binder.forField(lastName).asRequired().bind(Person::getLastName,
                Person::setLastName);
        binder.forField(street).asRequired().bind(Person::getStreet,
                Person::setStreet);
        binder.forField(city).asRequired().bind(Person::getCity,
                Person::setCity);
        binder.forField(country).asRequired().bind(Person::getCountry,
                Person::setCountry);
        binder.forField(phoneNumber).asRequired().bind(Person::getPhoneNumber,
                Person::setPhoneNumber);
        binder.forField(email).asRequired().bind(Person::getEmail,
                Person::setEmail);

        return new BinderCrudEditor<>(binder, form);
    }

    private void setupGrid() {
        crud.setDataProvider(DataProvider.ofCollection(personMap.values()));
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