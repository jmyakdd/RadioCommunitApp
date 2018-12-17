package radio.crte.com.radiocommunitapp.bean

class Contact {
    var name:String = ""
        set(value) {
            if(value==null||value.equals("")){
                field = ""
            }else{
                field = value
            }
        }
}