package com.example.projectwarung

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase

class WarungAdapter(val mCtx : Context, val layoutResId : Int, val wrgList: List<Warung> ) : ArrayAdapter<Warung> (mCtx, layoutResId, wrgList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInFlater : LayoutInflater = LayoutInflater.from(mCtx)

        val view : View = layoutInFlater.inflate(layoutResId, null)

        val tvNamaBrg : TextView = view.findViewById(R.id.tv_namabrg)
        val tvHarga : TextView = view.findViewById(R.id.tv_harga)
        val tvEdit : TextView = view.findViewById(R.id.tv_edit)

        val warung = wrgList[position]

        tvEdit.setOnClickListener {
            showUpdateDialog(warung)
        }

        tvNamaBrg.text = warung.barang
        tvHarga.text = warung.harga

        return view
    }

    fun showUpdateDialog(warung: Warung) {
        val builder = AlertDialog.Builder(mCtx)


        val inflater = LayoutInflater.from(mCtx)
        val view = inflater.inflate(R.layout.update_dialog, null)

        val etBarang = view.findViewById<EditText>(R.id.et_barang)
        val etHarga = view.findViewById<EditText>(R.id.et_harga)

        etBarang.setText(warung.barang)
        etHarga.setText(warung.harga)

        builder.setView(view)

        builder.setPositiveButton("Update"){p0,p1 ->
            val dbWrg = FirebaseDatabase.getInstance().getReference("warung")

            val barang = etBarang.text.toString().trim()
            val harga = etHarga.text.toString().trim()
            if(barang.isEmpty()) {
                etBarang.error = "Nama Barang diisi dulu!"
                etBarang.requestFocus()
                return@setPositiveButton
            }
            if(harga.isEmpty()) {
                etHarga.error = "Harga diisi dulu!"
                etHarga.requestFocus()
                return@setPositiveButton
            }

            val warung = Warung(warung.id, barang, harga)

            dbWrg.child(warung.id!!).setValue(warung)

            Toast.makeText(mCtx, "Data berhasil di update", Toast.LENGTH_SHORT).show()
        }

        builder.setNeutralButton("No"){p0,p1 ->

        }

        builder.setNegativeButton("Delete"){p0,p1 ->

            val dbWrg = FirebaseDatabase.getInstance().getReference("warung").child(warung.id)
            dbWrg.removeValue()

            Toast.makeText(mCtx, "Data berhasil dihapus", Toast.LENGTH_SHORT).show()

        }

        val alert = builder.create()
        alert.show()
    }
}