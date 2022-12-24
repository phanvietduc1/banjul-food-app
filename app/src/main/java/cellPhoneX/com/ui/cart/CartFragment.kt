package cellPhoneX.com.ui.cart

import android.R
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cellPhoneX.com.adapter.AdapterProduct
import cellPhoneX.com.databinding.FragmentCartBinding
import cellPhoneX.com.model.CartModel
import cellPhoneX.com.model.OrderModel
import cellPhoneX.com.model.ProductModel
import cellPhoneX.com.model.UserModel
import cellPhoneX.com.tools.Utils
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.FirebaseFirestore
import org.json.JSONException
import org.json.JSONObject
//import vn.momo.momo_partner.AppMoMoLib


class CartFragment : Fragment() {
    private var amount = "10000";
    private var fee = "0";
    var environment = 0;//developer default
    private var merchantName = "Demo SDK";
    private var merchantCode = "SCB01";
    private var merchantNameLabel = "Nhà cung cấp";
    private var description = "Thanh toán dịch vụ ABC";

    private var _binding: FragmentCartBinding? = null
    var loggedInUser: UserModel? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val notificationsViewModel =
            ViewModelProvider(this).get(CartViewModel::class.java)

        _binding = FragmentCartBinding.inflate(inflater, container, false)
        val root: View = binding.root

        notificationsViewModel.text.observe(viewLifecycleOwner) {
        }

        loggedInUser = Utils.get_logged_in_user()


        get_cart_data()
        get_data()

//        AppMoMoLib.getInstance().setEnvironment(AppMoMoLib.ENVIRONMENT.DEVELOPMENT);

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    //Get token through MoMo app
    private fun requestPayment() {
//        AppMoMoLib.getInstance().setAction(AppMoMoLib.ACTION.PAYMENT)
//        AppMoMoLib.getInstance().setActionType(AppMoMoLib.ACTION_TYPE.GET_TOKEN)
////        if (edAmount.getText().toString() != null && edAmount.getText().toString().trim().length() !== 0) {
////            amount = edAmount.getText().toString().trim();
////        }
//
//        val eventValue: MutableMap<String, Any> = HashMap()
//        //client Required
//        eventValue["merchantname"] =
//            merchantName //Tên đối tác. được đăng ký tại https://business.momo.vn. VD: Google, Apple, Tiki , CGV Cinemas
//        eventValue["merchantcode"] =
//            merchantCode //Mã đối tác, được cung cấp bởi MoMo tại https://business.momo.vn
//        eventValue["amount"] = 3 //Kiểu integer
//        eventValue["orderId"] =
//            "orderId123456789" //uniqueue id cho Bill order, giá trị duy nhất cho mỗi đơn hàng
//        eventValue["orderLabel"] = "Mã đơn hàng" //gán nhãn
//
//        //client Optional - bill info
//        eventValue["merchantnamelabel"] = "Dịch vụ" //gán nhãn
//        eventValue["fee"] = 0 //Kiểu integer
//        eventValue["description"] = description //mô tả đơn hàng - short description
//
//        //client extra data
//        eventValue["requestId"] = merchantCode + "merchant_billId_" + System.currentTimeMillis()
//        eventValue["partnerCode"] = merchantCode
//        //Example extra data
//        val objExtraData = JSONObject()
//        try {
//            objExtraData.put("site_code", "008")
//            objExtraData.put("site_name", "CGV Cresent Mall")
//            objExtraData.put("screen_code", 0)
//            objExtraData.put("screen_name", "Special")
//            objExtraData.put("movie_name", "Kẻ Trộm Mặt Trăng 3")
//            objExtraData.put("movie_format", "2D")
//        } catch (e: JSONException) {
//            e.printStackTrace()
//        }
//        eventValue["extraData"] = objExtraData.toString()
//        eventValue["extra"] = ""
//        AppMoMoLib.getInstance().requestMoMoCallBack(activity, eventValue)
    }

    //Get token callback from MoMo app an submit to server side
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == AppMoMoLib.getInstance().REQUEST_CODE_MOMO && resultCode == -1) {
//            if (data != null) {
//                if (data.getIntExtra("status", -1) == 0) {
//                    //TOKEN IS AVAILABLE
//                    tvMessage.setText("message: " + "Get token " + data.getStringExtra("message"))
//                    val token = data.getStringExtra("data") //Token response
//                    val phoneNumber = data.getStringExtra("phonenumber")
//                    var env = data.getStringExtra("env")
//                    if (env == null) {
//                        env = "app"
//                    }
//                    if (token != null && token != "") {
//                        // TODO: send phoneNumber & token to your server side to process payment with MoMo server
//                        // IF Momo topup success, continue to process your order
//                    } else {
//                        tvMessage.setText("message: " + this.getString(R.string.not_receive_info))
//                    }
//                } else if (data.getIntExtra("status", -1) == 1) {
//                    //TOKEN FAIL
//                    val message =
//                        if (data.getStringExtra("message") != null) data.getStringExtra("message") else "Thất bại"
//                    tvMessage.setText("message: $message")
//                } else if (data.getIntExtra("status", -1) == 2) {
//                    //TOKEN FAIL
//                    tvMessage.setText("message: " + this.getString(R.string.not_receive_info))
//                } else {
//                    //TOKEN FAIL
//                    tvMessage.setText("message: " + this.getString(R.string.not_receive_info))
//                }
//            } else {
//                tvMessage.setText("message: " + this.getString(R.string.not_receive_info))
//            }
//        } else {
//            tvMessage.setText("message: " + this.getString(R.string.not_receive_info_err))
//        }
//    }


    private fun get_data() {
        var tongprice = 0
        val loggedInUser = Utils.get_logged_in_user()

        binding.name.setText(loggedInUser.last_name)
        binding.phone.setText(loggedInUser.phone_number)
        binding.address.setText(loggedInUser.address)
        for (i in products.indices) {
            tongprice += products[i].price*products[i].quantity
        }
        binding.tongtien.setText("Tổng tiền:   " + tongprice.toString() + "  đ")
    }

    private fun remove_product_to_cart(product_id: String) {
        val list_cart_item =
            CartModel.find(CartModel::class.java, "productId = ?", product_id)
        list_cart_item[0].quantity = list_cart_item[0].quantity - 1

        if (list_cart_item[0].quantity == 0) {
            return
        }

        try {
            //cart_item.save();
            CartModel.save(list_cart_item[0])
            Toast.makeText(context, "Product remove to cart", Toast.LENGTH_SHORT).show()
        } catch (e: java.lang.Exception) {
            Toast.makeText(context, "Failed yo save because " + e.message, Toast.LENGTH_SHORT)
                .show()
        }
    }

    var cartModels: List<CartModel>? = null
    var products: MutableList<ProductModel> = ArrayList<ProductModel>()

    private fun get_cart_data() {
        cartModels = try {
            CartModel.listAll(CartModel::class.java)
        } catch (e: Exception) {
            Toast.makeText(activity, "Failed because " + e.message, Toast.LENGTH_SHORT).show()
            activity?.finish()
            return
        }
        if (cartModels == null) {
            Toast.makeText(activity, "Failed", Toast.LENGTH_SHORT).show()
            activity?.finish()
            return
        }
        for (c in cartModels!!) {
            val p = ProductModel()
            p.title = c.product_name
            p.category = ""
            p.details = ""
            p.quantity = c.quantity
            try {
                p.price = Integer.valueOf(c.product_price)
            } catch (e: Exception) {
            }
            p.product_id = c.product_id
            p.photo = c.product_photo
            products.add(p)
        }
        feed_cart_data()
    }

    var recyclerView: RecyclerView? = null
    private var mAdapter: AdapterProduct? = null
    var submit_order: Button? = null

    private fun feed_cart_data() {
        recyclerView = binding.cartProducts
        recyclerView!!.layoutManager = LinearLayoutManager(activity)
        //recyclerView.addItemDecoration(new SpacingItemDecoration(2, Tools.dpToPx(this, 8), true));
        recyclerView!!.setHasFixedSize(true)
        recyclerView!!.isNestedScrollingEnabled = false
        mAdapter = AdapterProduct(products, activity, "1")
        recyclerView!!.adapter = mAdapter
        submit_order = binding.submitOrder
        submit_order!!.setOnClickListener {
//            submit_order()
            requestPayment()
        }

        mAdapter!!.setOnRemoveItemClickListener(AdapterProduct.OnRemoveItemClickListener {
            refresh()
        })
    }

    fun refresh(){
        products.clear()
        get_cart_data()
        get_data()
    }

    var CUSTOMER_ORDERS = "CUSTOMER_ORDERS"
    var db = FirebaseFirestore.getInstance()
    var progressDialog: ProgressDialog? = null

    private fun submit_order() {
        val orderModel = OrderModel()
        orderModel.order_id = db.collection(CUSTOMER_ORDERS).document().id
        orderModel.customer = loggedInUser
        orderModel.cart = cartModels
        orderModel.money = binding.tongtien.text.toString()
        progressDialog = ProgressDialog(activity)
        progressDialog!!.setTitle("Please wait....")
        progressDialog!!.setCancelable(false)
        progressDialog!!.show()
        db.collection(CUSTOMER_ORDERS).document(orderModel.order_id).set(orderModel)
            .addOnSuccessListener(OnSuccessListener {
                Toast.makeText(
                    activity,
                    "Order Submitted successfully!",
                    Toast.LENGTH_SHORT
                ).show()
                progressDialog!!.hide()
                progressDialog!!.dismiss()
                try {
                    CartModel.deleteAll(CartModel::class.java)
                } catch (e: Exception) {
                    Toast.makeText(
                        activity,
                        "Failed to clear the cart.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                return@OnSuccessListener
            }).addOnFailureListener(OnFailureListener { e ->
                progressDialog!!.hide()
                progressDialog!!.dismiss()
                Toast.makeText(
                    activity,
                    "Failed to submit order because " + e.message,
                    Toast.LENGTH_SHORT
                ).show()
                return@OnFailureListener
            })
    }
}