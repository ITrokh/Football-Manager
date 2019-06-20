/*
Created by IntelliJ IDEA.
By trohi
Date: 21.05.2019 10:18
Project Name: football
*/
package com.mcw.football.domain.util;

import com.mcw.football.domain.User;

public abstract class MessageHelper {

    public static String getAuthorName(User author){
        return  author!=null? author.getUsername():"<none>";//если автор есть, то берем его имя, иначе <none>
    }
}
