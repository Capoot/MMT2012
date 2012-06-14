
$(document).ready(function (){
    var app = new App();

    $("input[type=submit]").click(function(event){
        $('#errorSpace').empty();
        if ($("#name").val() == "") {
            errorMessage("Title must be set");
            event.preventDefault();
            return;
        }
        if ($("#video").val() == "") {
            errorMessage("Video must be selected");
            event.preventDefault();
            return;
        }
        if ($("#lat").val() == "" || $("#lng").val() == "") {
            errorMessage("Location must be chosen.");
            event.preventDefault();
            return;
        }
    });

    var progress = $('.progress');
    progress.hide();

    $("form").ajaxForm({
        beforeSend: function() {
            progress.show();
            $('input').attr('disabled', 'disabled');
            $('button').attr('disabled', 'disabled');
        },
        uploadProgress: function(event, position, total, percentComplete) {
            progress.css('width', percentComplete);
        },
        complete: function(xhr) {
            window.location.href = "./index.htm#upload";
        },
        error: function() {
            errorMessage("upload failed");
            progress.hide();
            $('input').removeAttr('disabled');
            $('button').removeAttr('disabled');
        }
    });
});

function errorMessage(message) {

    var inner = '<div class="alert alert-error">' +
        '<a class="close" data-dismiss="alert" href="#">×</a>' +
        '<h4 class="alert-heading">Error!</h4>' +
        message +
        '</div>';

    $('#errorSpace').append(inner);
}

var App = function() {
    this.map = null;
    this.initialLocation = new google.maps.LatLng(52, 13);
    this.marker = null;
    this.geocoder = new google.maps.Geocoder();

    var myOptions = {
        zoom: 8,
        center: this.initialLocation,
        mapTypeId: google.maps.MapTypeId.ROADMAP,
        disableDefaultUI: true,
        scaleControl: true,
        zoomControl: true,
        panControl: true
    };

    this.map = new google.maps.Map(document.getElementById("map_canvas"), myOptions);
    this.showUserOnMap();

    var that = this;
    $("#geoPicker").click(function(event){
        event.preventDefault();
        that.encodeAndShowLocation(true);
    });
    $("#location").change(function() {
        that.encodeAndShowLocation(false);
    });
};

App.prototype.showUserOnMap = function() {
    var self = this;
    if (!navigator.geolocation) {
        return;
    }
    navigator.geolocation.getCurrentPosition(function(position) {
        self.showPosition(position.coords.latitude, position.coords.longitude);
    });
};

App.prototype.showPosition = function(lat, lng) {
    var latlng = new google.maps.LatLng(lat, lng);
    this.map.setCenter(latlng);
    this.map.setZoom(12);
};

App.prototype.ensureMarker = function() {
    if (this.marker != null) {
        return;
    }
    this.marker = new google.maps.Marker({
        position: this.map.getCenter(),
        map: this.map,
        title: "My position",
        draggable: true
    });
    var that = this;
    google.maps.event.addListener(this.marker, 'dragend', function() {
        that.markerLocationChangedByUser();
    });
};

App.prototype.encodeAndShowLocation = function(errorOnEmpty) {
    var location = $('#location').val();

    if (location == "") {
        if (errorOnEmpty) {
            alert("You have to enter a valid location");
        }
        return;
    }

    var that = this;
    this.geocoder.geocode({'address' : location}, function(results, status){
        if (status == google.maps.GeocoderStatus.ZERO_RESULTS) {
            alert("location not found");
            return;
        }
        if (status != google.maps.GeocoderStatus.OK) {
            alert("Some error occured, status:" + status);
        }

        that.ensureMarker();
        that.setMarkerPosition(results[0].geometry.location)
    });
};

App.prototype.setMarkerPosition = function(position) {
    this.marker.setPosition(position);
    this.showPosition(position.lat(), position.lng());
    this.setFormLatLng(position);
};

App.prototype.markerLocationChangedByUser = function() {
    this.setFormLatLng(this.marker.getPosition());
};

App.prototype.setFormLatLng = function(position) {
    $('#lat').val(position.lat());
    $('#lng').val(position.lng());
}