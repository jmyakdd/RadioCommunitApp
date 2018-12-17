package radio.crte.com.radiocommunitapp.activity

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_contacts.*
import radio.crte.com.radiocommunitapp.R
import radio.crte.com.radiocommunitapp.adapter.ContactAdapter
import radio.crte.com.radiocommunitapp.bean.Contact

class ContactsActivity : BaseActivity() {

    var data = ArrayList<Contact>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contacts)
        var adapter = ContactAdapter(this,data)
        contact_recyclerview.adapter = adapter
        contact_recyclerview.layoutManager = LinearLayoutManager(this)

        for(i in 0..10){
            var contact = Contact()
            contact.name = "item${i}"
            data.add(contact)
        }
        adapter.notifyDataSetChanged()
    }
}
