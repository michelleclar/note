package org.carl.documents.model;

import org.carl.generated.tables.pojos.Documents;

public class Document extends Documents {
    public Document(){

    }
    public Document(Integer id){
        this.setId(id);
    }
    Integer userId;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
