package com.zlst.demo.polo.mybook;

import com.zlst.demo.polo.Chapter;

public class EditableChapter extends Chapter {
    private boolean change;

    public boolean isChange() {
        return change;
    }

    public boolean isDisable() {
        return disable;
    }

    public void setDisable(boolean disable) {
        this.disable = disable;
    }

    private boolean disable;

    public EditableChapter(){
        this.disable = false;
    }
    @Override
    public String toString() {
        return "EditableChapter{" +
                "change=" + change +
                '}';
    }

    public void setChange(boolean change) {
        this.change = change;
    }

}
