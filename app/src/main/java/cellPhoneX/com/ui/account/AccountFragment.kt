package cellPhoneX.com.ui.account

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import cellPhoneX.com.activity.*
import cellPhoneX.com.databinding.FragmentAccountBinding
import cellPhoneX.com.model.UserModel
import cellPhoneX.com.tools.Utils

class AccountFragment : Fragment() {

    private var _binding: FragmentAccountBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val accountViewModel =
            ViewModelProvider(this).get(AccountViewModel::class.java)

        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        val root: View = binding.root

        accountViewModel.text.observe(viewLifecycleOwner) {

        }

        binding.button01.setOnClickListener {
            gotoCusOrders()
        }

        binding.button1.setOnClickListener {
            gotoOrders()
        }
        binding.button0.setOnClickListener {
            gotoInfo()
        }

        binding.button3.setOnClickListener {
            gotoAdd()
        }

        binding.button2.setOnClickListener {
            gotoChangepass()
        }

        binding.dangxuat.setOnClickListener {
            try {
                UserModel.deleteAll(UserModel::class.java)
                Toast.makeText(activity, "Logged you out successfully!", Toast.LENGTH_LONG).show()
            } catch (e: Exception) {
                Toast.makeText(
                    activity,
                    "Failed to Log you out because " + e.message,
                    Toast.LENGTH_LONG
                ).show()
            }
            activity?.finish()
        }

        var loggedInUser = Utils.get_logged_in_user()
        if (loggedInUser.user_type == "admin") {
            binding.button1.visibility = View.VISIBLE
            binding.button3.visibility = View.VISIBLE
            binding.button01.visibility = View.GONE
        }

        return root
    }

    fun gotoAdd(){
        val i = Intent(activity, FoodAddActivity::class.java)
        this.startActivity(i)
    }

    fun gotoChangepass(){
        val i = Intent(activity, ChangePasswordActivity::class.java)
        this.startActivity(i)
    }

    fun gotoInfo(){
        val i = Intent(activity, InfoActivity::class.java)
        this.startActivity(i)
    }

    fun gotoOrders(){
        val i = Intent(activity, AdminOrdersActivity::class.java)
        this.startActivity(i)
    }

    fun gotoCusOrders(){
        val i = Intent(activity, CustomerOrdersActivity::class.java)
        this.startActivity(i)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}