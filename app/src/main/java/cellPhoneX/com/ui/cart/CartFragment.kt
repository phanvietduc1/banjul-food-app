package cellPhoneX.com.ui.cart

import android.app.ProgressDialog
import android.os.Bundle
import android.os.StrictMode
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
import cellPhoneX.com.zalo.Api.CreateOrder
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.FirebaseFirestore
import org.json.JSONObject
import vn.zalopay.sdk.Environment
import vn.zalopay.sdk.ZaloPayError
import vn.zalopay.sdk.ZaloPaySDK
import vn.zalopay.sdk.listeners.PayOrderListener

open class CartFragment : Fragment() {
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

        var loggedInUser = Utils.get_logged_in_user()
        if (loggedInUser.user_type == "user") {
            binding.submitOrder.visibility = View.VISIBLE
            binding.submitOrder2.visibility = View.VISIBLE
        }


//        AppMoMoLib.getInstance().setEnvironment(AppMoMoLib.ENVIRONMENT.DEVELOPMENT);

        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

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
    var submit_order2: Button? = null

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
            submit_order("")
        }

        submit_order2 = binding.submitOrder2
        submit_order2!!.setOnClickListener {
            requestZalo(binding.tongtien.text.toString())
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

    fun requestZalo(money: String){
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        var string = money
        var result = string.filter { it.isDigit() }

        // ZaloPay SDK Init
        ZaloPaySDK.init(2553, Environment.SANDBOX)

        val orderApi = CreateOrder()

        try {
            val data: JSONObject = orderApi.createOrder("1000")

            val code = data.getString("return_code")

            if (code == "1") {
                var token = data.getString("zp_trans_token")
                ZaloPaySDK.getInstance().payOrder(
                    requireActivity(),
                    token,
                    "demozpdk://app",
                    object : PayOrderListener {
                        override fun onPaymentSucceeded(
                            transactionId: String,
                            transToken: String,
                            appTransID: String
                        ) {
                            Toast.makeText(activity, "Success", Toast.LENGTH_SHORT).show()
                            submit_order(token)
                        }

                        override fun onPaymentCanceled(zpTransToken: String, appTransID: String) {
                            Toast.makeText(activity, "Cancel", Toast.LENGTH_SHORT).show()
                        }

                        override fun onPaymentError(
                            zaloPayError: ZaloPayError,
                            zpTransToken: String,
                            appTransID: String
                        ) {
                            Toast.makeText(activity, "Error", Toast.LENGTH_SHORT).show()
                        }
                    })
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    private fun submit_order(zalotoken: String) {
        val orderModel = OrderModel()
        orderModel.order_id = db.collection(CUSTOMER_ORDERS).document().id
        orderModel.customer = loggedInUser
        orderModel.cart = cartModels
        orderModel.money = binding.tongtien.text.toString()
        orderModel.zalotoken = zalotoken
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