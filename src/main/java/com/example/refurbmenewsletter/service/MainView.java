package com.example.refurbmenewsletter.service;

import com.example.refurbmenewsletter.domain.Customer;
import com.example.refurbmenewsletter.repository.CustomerRepository;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import lombok.val;

import java.util.function.Predicate;


@Route("/")
public class MainView extends VerticalLayout{
    private final CustomerRepository customerRepository;

    private TextField firstNameField;
    private TextField lastNameField;
    private TextField emailField;
    private Checkbox acceptedPrivacyPolicyBox;

    private Button acceptButton;

    public MainView(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
        setWidthFull();
        setHeightFull();
        setAlignItems(Alignment.CENTER);

        getStyle().set("background-image", "url(\"https://cdn.discordapp.com/attachments/621043978510270474/1110941961663299664/SaschaEleven_a_lush_green_forest_a4c2cb72-d17d-4a1a-9241-6cc5113b20e8.png");

        firstNameField = new TextField("Vorname");
        firstNameField.setWidth("45%");
        lastNameField = new TextField("Nachname");
        lastNameField.setWidth("45%");

        StreamResource imageResource = new StreamResource("logo",
                () -> getClass().getResourceAsStream("/images/logo.png"));

        Image image = new Image(imageResource, "RefurbMe Logo");
        image.getStyle().set("padding-top", "10%");
        image.setWidthFull();
        HorizontalLayout imageLayout = new HorizontalLayout(image);

        imageLayout.setWidthFull();
        imageLayout.setHeight("300px");

        HorizontalLayout nameLayout = new HorizontalLayout(firstNameField, lastNameField);
        nameLayout.setWidthFull();
        nameLayout.setJustifyContentMode(JustifyContentMode.BETWEEN);

        emailField = new TextField("Email");
        emailField.setMaxWidth(100, Unit.PERCENTAGE);
        emailField.setMinWidth(100, Unit.PERCENTAGE);

        acceptedPrivacyPolicyBox = new Checkbox("Hiermiet bestätige ich, dass ich die Datenschutzerklärung gelesen habe und akzeptiere.");

        acceptButton = new Button("Absenden");
        acceptButton.addClickListener(event -> addCustomer());
        acceptButton.setEnabled(false);
        HorizontalLayout acceptButtonLayout = new HorizontalLayout(acceptButton);
        acceptButtonLayout.setWidthFull();
        acceptButtonLayout.setAlignItems(Alignment.CENTER);
        acceptButtonLayout.setJustifyContentMode(JustifyContentMode.CENTER);

        VerticalLayout form = new VerticalLayout(image, nameLayout,  emailField, acceptedPrivacyPolicyBox, acceptButtonLayout);
        form.getStyle().set("background-color", "white");
        form.getStyle().set("margin-top", "5%");
        form.setHeight(60, Unit.PERCENTAGE);
        form.setWidth(22, Unit.PERCENTAGE);
        form.getStyle().set("filter", "drop-shadow(0px 0px 120px #000)");
        add(form);

        setupChangeListener();
    }

    public void addCustomer() {
        val email = emailField.getValue();
        val firstName = firstNameField.getValue();
        val lastName = lastNameField.getValue();
        boolean acceptedPrivacyPolicy = acceptedPrivacyPolicyBox.getValue();

        if(customerRepository.existsByEmailIgnoreCase(email)) {
            throw new RuntimeException("Die E-Mail %s ist bereits vergeben".formatted(email));
        }

        val customer = new Customer(email, firstName, lastName, acceptedPrivacyPolicy);
        customerRepository.save(customer);

        emailField.clear();
        firstNameField.clear();
        lastNameField.clear();
        acceptedPrivacyPolicyBox.setValue(false);

        System.out.println(customer);

    }

    public void check() {
        val email = emailField.getValue();
        val firstName = firstNameField.getValue();
        val lastName = lastNameField.getValue();

        if(email == null || firstName == null|| lastName == null) {
            acceptButton.setEnabled(false);
            return;
        }
        if(!(email.isBlank() || firstName.isBlank() || lastName.isBlank()) && acceptedPrivacyPolicyBox.getValue()) {
            acceptButton.setEnabled(true);
            return;
        }
        acceptButton.setEnabled(false);
    }

    public void setupChangeListener() {
        firstNameField.addValueChangeListener(value -> check());
        lastNameField.addValueChangeListener(value -> check());
        emailField.addValueChangeListener(value -> check());
        acceptedPrivacyPolicyBox.addValueChangeListener(value -> check());
    }
}
