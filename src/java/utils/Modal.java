package utils;

import com.google.gson.Gson;

/**
 *
 * @author Gabriel
 */
public final class Modal {

    private String title;
    private String body;
    public Button primaryBtn;
    public Button secondaryBtn;

    public class Button {

        public String text;
        public String action;
    }

    public Modal(String title, String body) {
        this.title = title;
        this.body = body;
        this.primaryBtn = new Button();
        this.secondaryBtn = new Button();
        this.primaryBtn.text = "Ok";
        this.primaryBtn.action = "$('#modal').modal('toggle')";
        this.secondaryBtn.text = "Cancelar";
        this.secondaryBtn.action = " $('#modal').modal('toggle')";
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getJson() {
        return new Gson().toJson(this, Modal.class);
    }
}
