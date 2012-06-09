
$(document).ready(function (){
    var app = new App();

    $('#video').hide();

    $('#closeVideo').click(function(){
        $('#videoMap').removeClass('span6');
        $('#videoMap').addClass('span12');
        $('#video').hide();
    });
});

var App = function() {
    this.map = null;
    this.initialLocation = new google.maps.LatLng(52, 13);
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

    this.tick();
};

App.prototype.tick = function() {
    var that = this;
    jQuery.getJSON("http://localhost:8081/videos", function(data) {

        $.each(that.cameras, function(id, vals) {
            vals.dirty = true;
        });

        $.each(data, function(id, camera) {
            that.updateCamera(id, camera.lat, camera.lng, camera.url);
        });

        $.each(that.cameras, function(id, vals) {
            if (vals.dirty) {
                vals.setMap(null);
                delete that.cameras[id];
            }
        });
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
    this.userLocation = latlng;
    this.map.setCenter(latlng);
    this.map.setZoom(12);
};

App.prototype.updateCamera = function(id, lat, lng, url) {

    if (this.cameras[id] != undefined) {
        this.cameras[id].setPosition(new google.maps.LatLng(lat, lng));
    } else {
        var marker = new google.maps.Marker({
            position: new google.maps.LatLng(lat, lng),
            title:"Camera " + id,
            map: this.map,
            icon: "img/video.png"
        });
        google.maps.event.addListener(marker, 'click', function() {
            $('#videoMap').removeClass('span12');
            $('#videoMap').addClass('span6');
            $('#video').show();
            $('#video video').attr('src', url);
        });

        this.cameras[id] = marker;
    }
    this.cameras[id].dirty = false;
};

App.prototype.openInfoWindow = function() {
    infoWindow = $('#infoWindow');
    infoWindow.removeClass('top');
    infoWindow.addClass('right');
    infoWindow.show();


    flowplayer("player", "script/flowplayer-3.2.10.swf", {
        debug: true,
        clip: {

            provider: 'mapStream',
            url: 'metacafe'
        },

        plugins: {
            mapStream: {
                url: "flowplayer.rtmp-3.2.9.swf",
                netConnectionUrl: 'http://localhost/web/testData/video.mp4'
            }
        }
    });
    $('.videoClose').click(function() {
        infoWindow.hide();
    });
};

