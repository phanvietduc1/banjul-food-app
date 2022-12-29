package cellPhoneX.com.model;

import java.util.ArrayList;
import java.util.List;

public class OrderModel {
    public String order_id = "";
    public UserModel customer = new UserModel();
    public List<CartModel> cart = new ArrayList<>();
    public String money = "0";
    public String complete = "Chờ xác nhận";
    public String zalotoken = "";
}
