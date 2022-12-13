package cellPhoneX.com.ui.home

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import cellPhoneX.com.R
import cellPhoneX.com.activity.MainNewActivity
import cellPhoneX.com.activity.ProductActivity
import cellPhoneX.com.adapter.AdapterProduct
import cellPhoneX.com.adapter.PhotoAdapter
import cellPhoneX.com.databinding.FragmentHomeBinding
import cellPhoneX.com.model.Photo
import cellPhoneX.com.model.ProductModel
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.paulrybitskyi.persistentsearchview.PersistentSearchView
import com.paulrybitskyi.persistentsearchview.listeners.OnSearchConfirmedListener
import com.paulrybitskyi.persistentsearchview.listeners.OnSearchQueryChangeListener

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private var mAdapter: AdapterProduct? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var persistentSearchView: PersistentSearchView
    private var mQuery: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        get_data()
        init()
        persistentSearchView = binding.persistentSearchView
        initSearchView()

        binding.progressBar.visibility = View.GONE

        return root
    }

    fun init(){
        var photoAdapter = PhotoAdapter(activity, getListPhoto())
        binding.viewpager.adapter = photoAdapter

        binding.circle.setViewPager(binding.viewpager)
        photoAdapter.registerDataSetObserver(binding.circle.dataSetObserver)
    }

    private fun getListPhoto(): List<Photo>? {
        val list: MutableList<Photo> = java.util.ArrayList()
        list.add(Photo(R.drawable.sp0))
        list.add(Photo(R.drawable.sp1))
        return list
    }

    var products: List<ProductModel> = ArrayList()
    var db = FirebaseFirestore.getInstance()

    private fun get_data() {
        db.collection("PRODUCTS").get()
            .addOnSuccessListener(OnSuccessListener<QuerySnapshot> { queryDocumentSnapshots ->
                products = queryDocumentSnapshots.toObjects(ProductModel::class.java)
                initComponents()
            }).addOnFailureListener(OnFailureListener { initComponents() })
    }

    private val mOnSearchConfirmedListener = OnSearchConfirmedListener { searchView, query ->
        searchView.collapse()
        performSearch(query)
    }

    private fun performSearch(query: String) = with(binding) {
        progressBar.visibility = View.GONE
        recyclerView.alpha = 0f
//        progressBar.visibility = View.VISIBLE
//        sMedia = ArrayList<ThumbnailItem>()

        val runnable = Runnable {
            persistentSearchView.hideProgressBar(false)
            persistentSearchView.showLeftButton()

            mQuery = query
//            doGetListAssetByQ(query)
            progressBar.visibility = View.GONE
            recyclerView.animate()
                .alpha(1f)
                .setInterpolator(LinearInterpolator())
                .setDuration(300L)
                .start()
        }

        Handler().postDelayed(runnable, 1000L)

        persistentSearchView.hideLeftButton(false)
        persistentSearchView.showProgressBar()
    }

    private fun initSearchView() = with(persistentSearchView) {
        setOnLeftBtnClickListener((activity as MainNewActivity))
        setOnClearInputBtnClickListener((activity as MainNewActivity))
        setOnSearchConfirmedListener(mOnSearchConfirmedListener)
        setOnSearchQueryChangeListener(mOnSearchQueryChangeListener)
        setDismissOnTouchOutside(true)
        setDimBackground(true)
        isProgressBarEnabled = true
        isVoiceInputButtonEnabled = false
        isClearInputButtonEnabled = true
        setQueryInputGravity(Gravity.START or Gravity.CENTER)
    }

    private val mOnSearchQueryChangeListener = OnSearchQueryChangeListener { _, _, newQuery ->
        if(newQuery.isBlank()) {
            //
//            binding.progressBar.visibility = View.VISIBLE
            binding.recyclerView.alpha = 1f
//            gotMedia(ArrayList<ThumbnailItem>(), false)
        } else {
            performSearch(newQuery)
        }
    }

    private fun initComponents() {
        binding.recyclerView.layoutManager = GridLayoutManager(activity, 1)
        //recyclerView.addItemDecoration(new SpacingItemDecoration(2, Tools.dpToPx(this, 8), true));
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.setNestedScrollingEnabled(false)
        binding.progressBar.setVisibility(View.GONE)
//        binding.recyclerView.setVisibility(View.VISIBLE)
        mAdapter = AdapterProduct(products, activity, "0")
        binding.recyclerView.setAdapter(mAdapter)


        // on item list clicked
        mAdapter!!.setOnItemClickListener(AdapterProduct.OnItemClickListener { view, obj, position ->
            val i = Intent(activity, ProductActivity::class.java)
            i.putExtra("id", obj.product_id)
            activity?.startActivity(i)
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}