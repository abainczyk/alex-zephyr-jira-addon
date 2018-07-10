(function (AJS, $) {
  var API_URL = AJS.contextPath() + '/rest/alex/1.0/config';

  function updateConfig() {
    $('#messages').empty();

    var url = $('#url').attr('value');
    var email = $('#email').attr('value');
    var password = $('#password').attr('value');
    var issueType = $('#issueType').attr('value');
    var projectKey = $('#projectKey').attr('value');

    var data = JSON.stringify({
      url: url,
      email: email,
      password: password,
      issueType: issueType,
      projectKey: projectKey
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
      $('#email').val(config.email);
      $('#password').val(config.password);
      $('#issueType').val(config.issueType);
      $('#projectKey').val(config.projectKey);
    });

    // use the aui submit event so that the validation mechanism kicks in.
    $('#config-form').on('aui-valid-submit', function (event) {
      event.preventDefault();
      updateConfig();
    });
  });
})(AJS || {}, AJS.$ || jQuery);
