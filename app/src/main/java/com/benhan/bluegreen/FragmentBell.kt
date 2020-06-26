package com.benhan.bluegreen

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import kotlinx.android.synthetic.main.home_fragment_bell.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FragmentBell: Fragment() {

    val apiClient = ApiClient()
    val apiInterface = apiClient.getApiClient().create(ApiInterface::class.java)
    val bellDataList = ArrayList<BellData>()
    val sharedPreference = SharedPreference()
    var adapter: BellAdapter? = null
    var myEmail: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView =  inflater.inflate(R.layout.home_fragment_bell, container, false)

        myEmail = sharedPreference.getString(requireContext(), "email")


        val tree = rootView.findViewById<ImageView>(R.id.tree)
        val search = rootView.findViewById<ImageView>(R.id.search)
        val bell = rootView.findViewById<ImageView>(R.id.bell)
        val user = rootView.findViewById<ImageView>(R.id.user)

        tree.setImageResource(R.drawable.tree)
        search.setImageResource(R.drawable.search)
        bell.setImageResource(R.drawable.bell_selected)
        user.setImageResource(R.drawable.user)

        fun clickHandler(view: ImageView){



            view.setOnClickListener {

                when(view.id) {
                    R.id.tree -> {
                        Navigation.findNavController(rootView).navigate(R.id.from_bell_to_tree)
                    }
                    R.id.search -> {
                        Navigation.findNavController(rootView).navigate(R.id.from_bell_to_search)
                    }
                    R.id.bell -> {

                    }
                    R.id.user -> {
                        Navigation.findNavController(rootView).navigate(R.id.from_bell_to_user)
                    }
                }
            }
        }

        val permissionCheck = ContextCompat.checkSelfPermission(requireActivity(), android.Manifest.permission.READ_EXTERNAL_STORAGE)
        val permissionListener = object : PermissionListener {

            override fun onPermissionGranted() {
                startActivity(Intent(requireContext(), PlusActivity::class.java))

            }

            override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                Toast.makeText(requireContext(), "권한 거부\n" + deniedPermissions.toString(),
                    Toast.LENGTH_SHORT).show()
            }

        }
        val plus = rootView.findViewById<ImageView>(R.id.plus)
        plus.setOnClickListener {



            if(permissionCheck == PackageManager.PERMISSION_DENIED) {


                TedPermission.with(requireContext())
                    .setPermissionListener(permissionListener)
                    .setRationaleMessage("사진첩을 열기 위해서는 갤러리 접근 권한이 필요해요")
                    .setDeniedMessage("[설정] > [권한] 에서 권한을 허용할 수 있어요")
                    .setPermissions(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    .check()
            }


            else if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                val intent = Intent(requireActivity(), PlusGalleryActivity::class.java)
                startActivity(intent)
            }
        }

        clickHandler(tree)
        clickHandler(search)
        clickHandler(user)

        /*=========================================================================================================*/

        val recyclerview: RecyclerView = rootView.findViewById(R.id.recyclerview)
        adapter = BellAdapter(requireContext(), bellDataList)
        recyclerview.layoutManager = LinearLayoutManager(requireContext())
        recyclerview.itemAnimator = DefaultItemAnimator()
        recyclerview.adapter = adapter


        val onLoadMoreListener = object: HomeRecyclerAdapter.OnLoadMoreListener{
            override fun onLoadMore() {

                recyclerview!!.post(object : Runnable{
                    override fun run() {
                        val index = bellDataList.size-1
                        getMoreNotificationData(index)
                    }

                })
            }

        }

        adapter?.onLoadMoreListener = onLoadMoreListener





        if(adapter?.itemCount == 0)
        getNotificationData(0)



        return rootView

    }


    fun getNotificationData(index: Int) {

        val call: Call<ArrayList<BellData>> = apiInterface.getNotification(myEmail!!, index)
        call.enqueue(object : Callback<ArrayList<BellData>>{
            override fun onFailure(call: Call<ArrayList<BellData>>, t: Throwable) {

            }

            override fun onResponse(
                call: Call<ArrayList<BellData>>,
                response: Response<ArrayList<BellData>>
            ) {
                response.body()?.let { bellDataList.addAll(it) }
                adapter?.notifyDataSetChanged()
            }


        })


    }

    fun getMoreNotificationData(index: Int) {

        bellDataList.add(BellData("load"))
        adapter?.notifyItemInserted(bellDataList.size - 1)

        val newIndex = index + 1
        val call: Call<ArrayList<BellData>> = apiInterface.getNotification(myEmail!!, newIndex)
        call.enqueue(object : Callback<ArrayList<BellData>>{
            override fun onFailure(call: Call<ArrayList<BellData>>, t: Throwable) {

            }

            override fun onResponse(
                call: Call<ArrayList<BellData>>,
                response: Response<ArrayList<BellData>>
            ) {
                if(response.isSuccessful){
                    bellDataList.removeAt(bellDataList.size - 1)
                    val result: ArrayList<BellData>? = response.body()
                    if(result!!.size > 0 ){
                        bellDataList.addAll(result)
                    }else{
                        adapter!!.isMoreDataAvailable = false
                    }
                }
                adapter?.notifyDataChanged()
            }


        })


    }

}
