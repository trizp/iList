package com.example.projectwarung

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import com.google.firebase.database.*

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var etBarang : EditText
    private lateinit var etHarga : EditText
    private lateinit var btnSave : Button
    private lateinit var listWrg : ListView
    private lateinit var ref : DatabaseReference
    private lateinit var wrgList: MutableList<Warung>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.hide()

        ref = FirebaseDatabase.getInstance().getReference("warung")

        etBarang = findViewById(R.id.et_barang)
        etHarga = findViewById(R.id.et_harga)
        btnSave = findViewById(R.id.btn_save)
        listWrg = findViewById(R.id.lv_wrg)
        btnSave.setOnClickListener(this)

        wrgList = mutableListOf()

        ref.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    wrgList.clear()
                    for (h in snapshot.children){
                        val warung = h.getValue(Warung::class.java)
                        if (warung != null) {
                            wrgList.add(warung)
                        }
                    }

                    val adapter = WarungAdapter(this@MainActivity, R.layout.item_wrg, wrgList)
                    listWrg.adapter = adapter
                }
            }

        })

    }

    override fun onClick(p0: View?) {
        saveData()
    }

    private fun saveData() {
        val barang = etBarang.text.toString().trim()
        val harga = etHarga.text.toString().trim()

        if(barang.isEmpty()) {
            etBarang.error = "Isi Barang Dulu!"
            return
        }

        if(harga.isEmpty()) {
            etHarga.error = "Isi Harga Dulu!"
            return
        }

        val wrgId = ref.push().key

        val wrg = Warung(wrgId!!, barang, harga)

        if (wrgId != null) {
            ref.child(wrgId).setValue(wrg).addOnCompleteListener {
                Toast.makeText(applicationContext, "Data berhasil ditambahkan", Toast.LENGTH_SHORT).show()
            }
        }
    }
}