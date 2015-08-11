db.inventory.find().forEach(function(item){
    if ((item.locations.length > 0) && (item.locations[0] != null) && (item.inventoryType != null)) {
        var url = item.locations[0].url
        var usedInTitle = (item.attributes.TITLE != null) ? ((item.attributes.TITLE.toLowerCase().indexOf("used") != -1)
        ||(item.attributes.TITLE.toLowerCase().indexOf("certified") != -1)) : 0

        var newInTitle = (item.attributes.TITLE != null) ? (item.attributes.TITLE.toLowerCase().indexOf("new") != -1) : false

        var usedInUrl = (url != null) ? ((url.toLowerCase().indexOf("used") != -1) || (url.toLowerCase().indexOf("certified") != -1)) : false
        var newInUrl = (url != null) ? (url.toLowerCase().indexOf("new") != -1) : false


        var predictNew = ((newInTitle + newInUrl) > (usedInTitle + usedInUrl)) ? 1 : 0
        var year = (item.attributes.YEAR == null) ? predictNew : ((item.attributes.YEAR) > 2011 ? 1 : 0)

        var msrp = binary(item.attributes.MSRP != null)

        var millage = (item.attributes.MILEAGE == null) ? 1 : ((item.attributes.MILEAGE) > 5000 ? 0 : 1)

        var type = (item.inventoryType.toUpperCase() == "NEW") ? "NEW" : "USED"
        print(type + "," +binary(newInTitle) + "," +
            binary(usedInTitle) + "," + binary(newInUrl) + ","
            + binary(usedInUrl) + "," + year + "," + msrp + "," + millage)
    }
})

function binary(x) {
    return x ? 1 : 0;
}