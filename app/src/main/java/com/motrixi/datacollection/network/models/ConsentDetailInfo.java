package com.motrixi.datacollection.network.models;

import java.io.Serializable;

/**
 * author : Jason
 * date   : 2020/12/8 5:02 PM
 * desc   :
 */
public class ConsentDetailInfo implements Serializable {

    public boolean success;
    public int code;
    public String message;
    public ResultInfo result;

    @Override
    public String toString() {
        return "ConsentDetailInfo{" +
                "success=" + success +
                ", code=" + code +
                ", message='" + message + '\'' +
                ", result=" + result +
                '}';
    }


    public class ResultInfo implements Serializable {

        public int id;
        public String key;
        public ListInfo value;
        public String deleted_at;
        public String created_at;
        public String updated_at;
        public String language;

        @Override
        public String toString() {
            return "ResultInfo{" +
                    "id=" + id +
                    ", key='" + key + '\'' +
                    ", value=" + value +
                    ", deleted_at='" + deleted_at + '\'' +
                    ", created_at='" + created_at + '\'' +
                    ", updated_at='" + updated_at + '\'' +
                    ", language='" + language + '\'' +
                    '}';
        }
    }

    public class ListInfo implements Serializable {
        public String terms_content;
        public String options;
        public String cancel_button_text;
        public String confirm_button_text;
        public String option_button_text;
        public String back_button_text;
        public String terms_page_title;
        public String link_page_title;
        public String option_page_title;
        public String terms_link;

        @Override
        public String toString() {
            return "ListInfo{" +
                    "terms_content='" + terms_content + '\'' +
                    ", options='" + options + '\'' +
                    ", cancel_button_text='" + cancel_button_text + '\'' +
                    ", confirm_button_text='" + confirm_button_text + '\'' +
                    ", option_button_text='" + option_button_text + '\'' +
                    ", back_button_text='" + back_button_text + '\'' +
                    ", terms_page_title='" + terms_page_title + '\'' +
                    ", link_page_title='" + link_page_title + '\'' +
                    ", option_page_title='" + option_page_title + '\'' +
                    ", terms_link='" + terms_link + '\'' +
                    '}';
        }
    }
}
