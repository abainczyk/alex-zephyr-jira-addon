(function (AJS, $) {
  var API_URL = AJS.contextPath() + '/rest/alex/1.0/config';

  function updateConfig() {
    $('#messages').empty();

    var url = $('#url').attr('value');
    var issueType = $('#issueType').attr('value');

    var data = JSON.stringify({
      url: url,
      issueType: issueType
    });

    $.ajax({
      url: API_URL,
      type: 'PUT',
      contentType: 'application/json',
      data: data,
      processData: false
    }).done(function () {
      AJS.messages.success('#messages', {
        title: 'The configuration has been saved.'
      });
    }).error(function () {
      AJS.messages.error('#messages', {
        title: 'The configuration could not be saved.'
      });
    });
  }

  $(document).ready(function () {
    $.ajax({
      url: API_URL,
      dataType: 'json'
    }).done(function (config) {
      $('#url').val(config.url);
      $('#issueType').val(config.issueType);
    });

    // use the aui submit event so that the validation mechanism kicks in.
    $('#config-form').on('aui-valid-submit', function (event) {
      event.preventDefault();
      updateConfig();
    });
  });
})(AJS || {}, AJS.$ || jQuery);
