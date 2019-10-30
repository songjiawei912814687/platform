package com.personnel.domian.output;

import com.personnel.model.AddressBook;

public class AddressBookOutput extends AddressBook{
    private String sexName;

    public String getSexName() {
        if(getSex()!=null){
            switch (getSex()){
                case 0:
                    sexName="男";
                    break;
                case 1:
                    sexName="女";
                    break;
            }
        }
        return sexName;
    }

    public void setSexName(String sexName) {
        this.sexName = sexName;
    }
}
