package com.kenyrim.mvvm

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.kenyrim.mvvm.storage.DataBase
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), Listener {
    private var viewManager = LinearLayoutManager(this)
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val factory = MainViewModelFactory()
        viewModel = ViewModelProvider(this,factory).get(MainViewModel::class.java)




        loadContent()


        initialiseAdapter()
    }
    private fun initialiseAdapter(){
        recyclerView.layoutManager = viewManager
        observeData()
    }

    private fun observeData(){
        viewModel.lst.observe(this, {

            recyclerView.adapter= NoteRecyclerAdapter(viewModel, it, this)
        })
    }

    private fun addData(url:String){
        viewModel.add(url)
        recyclerView.adapter?.notifyDataSetChanged()
    }

    private fun loadContent() {

        GlobalScope.launch(Dispatchers.IO) {
            Parser().parseArticles(this@MainActivity)
        }
    }


    override fun getResult() {
        val db = DataBase(this)
        val list = db.selectAll()
        db.close()
        GlobalScope.launch(Dispatchers.Main) {
            for (item in list) {
                addData(item.enclosure)
            }
        }
    }
}