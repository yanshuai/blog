(function() {
  $('#search-btn').on('click', function() {
    var searchText = $('#searchText').val();
    window.open('http://www.baidu.com/s?wd=site%3Ablog.sartisty.name%20' + searchText);
  });

  $('#searchText').on('keydown', function(e) {
    if (13 === e.which) {
      var searchText = $('#searchText').val();
      window.open('http://www.baidu.com/s?wd=site%3Ablog.sartisty.name%20' + searchText);
    }
  });
})();
