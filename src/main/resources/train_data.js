db.inventory.find().forEach(function(item){
    if ((item.locations.length > 0) && (item.locations[0] != null) && (item.inventoryType != null)) {
        var url = item.locations[0].url
        var usedInTitle = (item.attributes.TITLE != null) ? ((item.attributes.TITLE.indexOf("used") != -1)
        ||(item.attributes.TITLE.indexOf("certified") != -1)) : 0

        var newInTitle = (item.attributes.TITLE != null) ? (item.attributes.TITLE.indexOf("new") != -1) : false

        var usedInUrl = (url != null) ? ((url.indexOf("used") != -1) || (url.indexOf("certified") != -1)) : false
        var newInUrl = (url != null) ? (url.indexOf("new") != -1) : false

        var year = (item.attributes.YEAR == null) ? -1 : ((item.attributes.YEAR) > 2011 ? 1 : 0)

        var msrp = binary(item.attributes.MSRP != null)

        var millage = (item.attributes.MILEAGE == null) ? -1 : ((item.attributes.MILEAGE) > 5000 ? 0 : 1)

        var type = (item.inventoryType.toUpperCase() == "NEW") ? "NEW" : "USED"
        print(type + "," +binary(usedInTitle) + "," +
            binary(newInTitle) + "," + binary(usedInUrl) + ","
            + binary(newInUrl) + "," + year + "," + msrp + "," + millage)
    }
})

function binary(x) {
    return x ? 1 : 0;
}