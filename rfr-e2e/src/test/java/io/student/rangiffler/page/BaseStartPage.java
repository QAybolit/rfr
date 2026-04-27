package io.student.rangiffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;

public abstract class BaseStartPage {

    final SelenideElement header = $("h1");
    final SelenideElement logo = $(".header__logo");
    final SelenideElement mainImage = $(".main__hero");
}
