package cellPhoneX.com.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import cellPhoneX.com.activity.ProductActivity
import cellPhoneX.com.adapter.AdapterProduct
import cellPhoneX.com.databinding.FragmentDashboardBinding
import cellPhoneX.com.model.ProductModel
import cellPhoneX.com.tools.Utils
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot


class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        dashboardViewModel.text.observe(viewLifecycleOwner) {
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        get_data()
    }

    override fun onDestroyView() {
        super.onDestroyView()
//        _binding = null
    }
    var db = FirebaseFirestore.getInstance()
    var products: List<ProductModel> = ArrayList()
    private fun get_data() {
        val loggedInUser = Utils.get_logged_in_user()
        if (loggedInUser.user_type == "admin") {
            db.collection("PRODUCTS")
                .whereEqualTo("category", "Iphone")
                .get()
                .addOnSuccessListener(OnSuccessListener<QuerySnapshot> { queryDocumentSnapshots ->
                    products = queryDocumentSnapshots.toObjects(ProductModel::class.java)
                    initComponentsIphone()
                }).addOnFailureListener(OnFailureListener { initComponentsIphone() })

            db.collection("PRODUCTS")
                .whereEqualTo("category", "Samsung")
                .get()
                .addOnSuccessListener(OnSuccessListener<QuerySnapshot> { queryDocumentSnapshots ->
                    products = queryDocumentSnapshots.toObjects(ProductModel::class.java)
                    initComponentsSs()
                }).addOnFailureListener(OnFailureListener { initComponentsSs() })

            db.collection("PRODUCTS")
                .whereEqualTo("category", "Xiaomi")
                .get()
                .addOnSuccessListener(OnSuccessListener<QuerySnapshot> { queryDocumentSnapshots ->
                    products = queryDocumentSnapshots.toObjects(ProductModel::class.java)
                    initComponentsxiao()
                }).addOnFailureListener(OnFailureListener { initComponentsxiao() })

            return
        }

        db.collection("PRODUCTS")
            .whereEqualTo("category", "Iphone")
            .whereNotEqualTo("hidden", "Ẩn")
            .get()
            .addOnSuccessListener(OnSuccessListener<QuerySnapshot> { queryDocumentSnapshots ->
                products = queryDocumentSnapshots.toObjects(ProductModel::class.java)
                initComponentsIphone()
            }).addOnFailureListener(OnFailureListener { initComponentsIphone() })

        db.collection("PRODUCTS")
            .whereEqualTo("category", "Samsung")
            .whereNotEqualTo("hidden", "Ẩn")
            .get()
            .addOnSuccessListener(OnSuccessListener<QuerySnapshot> { queryDocumentSnapshots ->
                products = queryDocumentSnapshots.toObjects(ProductModel::class.java)
                initComponentsSs()
            }).addOnFailureListener(OnFailureListener { initComponentsSs() })

        db.collection("PRODUCTS")
            .whereEqualTo("category", "Xiaomi")
            .whereNotEqualTo("hidden", "Ẩn")
            .get()
            .addOnSuccessListener(OnSuccessListener<QuerySnapshot> { queryDocumentSnapshots ->
                products = queryDocumentSnapshots.toObjects(ProductModel::class.java)
                initComponentsxiao()
            }).addOnFailureListener(OnFailureListener { initComponentsxiao() })
    }
    private var mAdapter: AdapterProduct? = null
    private fun initComponentsIphone() {
        val layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        binding.iphone.layoutManager = layoutManager
        //recyclerView.addItemDecoration(new SpacingItemDecoration(2, Tools.dpToPx(this, 8), true));
        binding.iphone.setHasFixedSize(true)
        binding.iphone.setNestedScrollingEnabled(true)
        mAdapter = AdapterProduct(products, activity, "0")
        binding.iphone.setAdapter(mAdapter)


        // on item list clicked
        mAdapter!!.setOnItemClickListener(AdapterProduct.OnItemClickListener { view, obj, position ->
            val i = Intent(activity, ProductActivity::class.java)
            i.putExtra("id", obj.product_id)
            activity?.startActivity(i)
        })
    }
    private fun initComponentsSs() {
        val layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)

        binding.samsung.layoutManager = layoutManager
        //recyclerView.addItemDecoration(new SpacingItemDecoration(2, Tools.dpToPx(this, 8), true));
        binding.samsung.setHasFixedSize(true)
        binding.samsung.setNestedScrollingEnabled(true)
        mAdapter = AdapterProduct(products, activity, "0")
        binding.samsung.setAdapter(mAdapter)


        // on item list clicked
        mAdapter!!.setOnItemClickListener(AdapterProduct.OnItemClickListener { view, obj, position ->
            val i = Intent(activity, ProductActivity::class.java)
            i.putExtra("id", obj.product_id)
            activity?.startActivity(i)
        })
    }
    private fun initComponentsxiao() {
        val layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        binding.xiaomi.layoutManager = layoutManager
        //recyclerView.addItemDecoration(new SpacingItemDecoration(2, Tools.dpToPx(this, 8), true));
        binding.xiaomi.setHasFixedSize(true)
        binding.xiaomi.setNestedScrollingEnabled(true)
        mAdapter = AdapterProduct(products, activity, "0")
        binding.xiaomi.setAdapter(mAdapter)


        // on item list clicked
        mAdapter!!.setOnItemClickListener(AdapterProduct.OnItemClickListener { view, obj, position ->
            val i = Intent(activity, ProductActivity::class.java)
            i.putExtra("id", obj.product_id)
            activity?.startActivity(i)
        })
    }
}