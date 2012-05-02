
$(document).ready(function (){

    var app = new App();
});

var App = function() {
    this.map = null;
    this.initialLocation = new google.maps.LatLng(52, 13);
    this.userLocation = null;
    this.cameras = {};
    this.infoWindow = null;

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
    this.addCamera(123);
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
    this.userLocation = latlng;
    this.map.setCenter(latlng);
    this.map.setZoom(12);
};

App.prototype.addCamera = function(id) {

    var marker = new google.maps.Marker({
        position: new google.maps.LatLng(52, 13),
        title:"Camera ###",
        map: this.map,
        icon: "img/video.png"
    });

    google.maps.event.addListener(marker, 'click', this.openInfoWindow);

    this.cameras[id] = marker;
};

App.prototype.openInfoWindow = function() {
    var info = '<h1>LiveStream</h1>'
        + '<video autoplay="autoplay">'
        + '<source src="testData/video.webm" type="video/webm" />'
        + '<source src="testData/video.mp4" type="video/mp4" />'
        + '<source src="testData/video.webm" type="video/ogg; codecs=\'theora, vobis\'" />'
        + '</video>'
        + '<button class="videoClose">close</button>';

    infoWindow = $('#infoWindow');
    infoWindow.html(info);
    infoWindow.removeClass('top');
    infoWindow.addClass('right');
    infoWindow.show();
    $('.videoClose').click(function() {
        infoWindow.hide();

    });
};

App.prototype.closeWindow = function() {
    this.infoWindow.close();
};