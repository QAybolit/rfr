package io.student.rangiffler.page.component;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.partialText;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$$;

public class SelectField {

    private final SelenideElement self;
    private final ElementsCollection options = $$("li[role='option']");

    public SelectField(SelenideElement self) {
        this.self = self;
    }

    @Step("Select option with text {text} from dropdown")
    public void selectOptionFromDropdownByText(String text) {
        this.self.click();
        this.options.find(partialText(text)).click();
    }

    @Step("Check selected option is equals to {location}")
    public void checkSelectedOption(String location) {
        this.self.shouldBe(visible).shouldHave(text(location));
    }
}
