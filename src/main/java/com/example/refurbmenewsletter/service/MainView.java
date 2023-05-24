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


@Route("/")
public class MainView extends VerticalLayout{
    private final CustomerRepository customerRepository;

    private TextField firstNameField;
    private TextField lastNameField;
    private TextField emailField;
    private Checkbox acceptedPrivacyPolicyBox;


    public MainView(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
        setWidthFull();
        setHeightFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setAlignItems(Alignment.CENTER);

        getStyle().set("background-image", "url(\"https://cdn.discordapp.com/attachments/621043978510270474/1110941961663299664/SaschaEleven_a_lush_green_forest_a4c2cb72-d17d-4a1a-9241-6cc5113b20e8.png");

        firstNameField = new TextField("Vorname");
        firstNameField.setMinWidth(40, Unit.PERCENTAGE);
        firstNameField.setMaxWidth(40, Unit.PERCENTAGE);
        lastNameField = new TextField("Nachname");
        lastNameField.setMinWidth(40, Unit.PERCENTAGE);
        lastNameField.setMaxWidth(40, Unit.PERCENTAGE);

        StreamResource imageResource = new StreamResource("logo",
                () -> getClass().getResourceAsStream("/images/logo.png"));

        Image image = new Image(imageResource, "RefurbMe Logo");
        image.getStyle().set("padding-top", "30%");
        image.setWidthFull();
        HorizontalLayout imageLayout = new HorizontalLayout(image);


        imageLayout.setWidthFull();
        imageLayout.setHeight("300px");

        HorizontalLayout div = new HorizontalLayout(firstNameField, lastNameField);
        div.setMinWidth(100, Unit.PERCENTAGE);

        emailField = new TextField("Email");
        emailField.setMaxWidth(100, Unit.PERCENTAGE);
        emailField.setMinWidth(100, Unit.PERCENTAGE);

        acceptedPrivacyPolicyBox = new Checkbox("Hiermiet bestätige ich, dass ich die Datenschutzerklärung gelesen habe und akzeptiere.");

        Button acceptButton = new Button("Absenden");
        acceptButton.addClickListener(event -> addCustomer());
        HorizontalLayout acceptButtonLayout = new HorizontalLayout(acceptButton);
        acceptButtonLayout.setWidthFull();
        acceptButtonLayout.setAlignItems(Alignment.CENTER);
        acceptButtonLayout.setJustifyContentMode(JustifyContentMode.CENTER);

        VerticalLayout form = new VerticalLayout(image, div,  emailField, acceptedPrivacyPolicyBox, acceptButtonLayout);
        form.getStyle().set("background-color", "white");
        form.setHeightFull();
        form.setWidth(22, Unit.PERCENTAGE);
        add(form);

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
}
