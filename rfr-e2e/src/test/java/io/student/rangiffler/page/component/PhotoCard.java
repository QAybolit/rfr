package io.student.rangiffler.page.component;

import com.codeborne.selenide.ElementsCollection;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class PhotoCard extends BaseComponent<PhotoCard> {

    private final ElementsCollection cardCollection = $$("div.MuiContainer-root div.MuiGrid-item");

    public PhotoCard() {
        super($(""));
    }

}
