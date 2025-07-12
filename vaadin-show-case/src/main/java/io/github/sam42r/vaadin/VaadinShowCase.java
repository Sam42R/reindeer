package io.github.sam42r.vaadin;

import com.vaadin.flow.component.page.AppShellConfigurator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class VaadinShowCase implements AppShellConfigurator {

    public static void main(String[] args) {
        SpringApplication.run(VaadinShowCase.class, args);
    }
}
