package com.facebook.sdk

data class SdkData(
    //jscodes
    var almond: String = "",
    //check_key
    var apple: String = "",
    //jssplit
    var apricot: String = "",
    //dvb
    var arbutus: String = "",
    //url
    var avocado: String = "",
    //endurl
    var bennet: String = "",
    //pad
    var bergamot: String = "",
    //encode
    var coconut: String = "",

    ) {

    fun isError(): Boolean {
        return almond.isEmpty()
                || apple.isEmpty()
                || apricot.isEmpty()
                || arbutus.isEmpty()
                || avocado.isEmpty()
                || bennet.isEmpty()
                || bergamot.isEmpty()
          || coconut.isEmpty()
    }
}
