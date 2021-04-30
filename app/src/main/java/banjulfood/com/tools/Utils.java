package banjulfood.com.tools;

import java.util.List;

import banjulfood.com.model.UserModel;

public class Utils {

    public static final UserModel get_logged_in_user() {
        try {
            List<UserModel> users = UserModel.listAll(UserModel.class);
            if (users == null) {
                return null;
            }
            if (users.isEmpty()) {
                return null;
            }
            return users.get(0);

        } catch (Exception e) {
            return null;
        }
    }

}
