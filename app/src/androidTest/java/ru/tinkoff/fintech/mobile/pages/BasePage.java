package ru.tinkoff.fintech.mobile.pages;

import ru.tinkoff.fintech.mobile.elements.DrawerElement;

public abstract class BasePage {

    private final DrawerElement drawer;

    public BasePage() {
        this.drawer = new DrawerElement();
    }

    public DrawerElement getDrawer() {
        return drawer;
    }

}
