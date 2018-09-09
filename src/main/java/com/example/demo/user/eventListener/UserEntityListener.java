package com.example.demo.user.eventListener;

import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;
import java.io.IOException;

public class UserEntityListener {

    @PostUpdate
    void onPostUpdate(Object o) throws IOException {


    }

    @PostRemove
    void onPostRemove(Object o) {

    }

}
