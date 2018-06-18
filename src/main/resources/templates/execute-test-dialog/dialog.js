(function (AJS, $) {
  var apiUrl = AJS.contextPath() + '/rest/alex/1.0/execute';

  var dialogEl = $('#add-watchers-dialog');
  var executeButtonEl = dialogEl.find('.execute');
  var messagesEl = dialogEl.find('.messages');

  var data = null;

  executeButtonEl.click(function () {
    console.log('execute', data);
    messagesEl.empty();
    executeButtonEl.prop('disabled', true);

    if (data == null) {
      AJS.messages.info(messagesEl, {
        title: 'Please select a target URL.'
      });
      executeButtonEl.prop('disabled', false);
      return;
    }

    $.ajax({
      url: apiUrl,
      type: 'POST',
      contentType: 'application/json',
      data: JSON.stringify(data),
      processData: false
    }).done(function () {
      messagesEl.empty();
      AJS.messages.success(messagesEl, {
        title: 'The test started successfully.'
      });
      dialogEl.find('.cancel').text('Close');
    }).error(function (err) {
      messagesEl.empty();

      var data = JSON.parse(err.responseText);
      AJS.messages.error(messagesEl, {
        title: 'The test could not be started. ' + data.message
      });
    }).complete(function() {
      executeButtonEl.prop('disabled', false);
    });
  });

  dialogEl.find('.alex-list-group-item').click(function() {
    var self = $(this);

    data = getFormData();
    data.urlId = parseInt(self.data('id'));

    dialogEl.find('.alex-list-group-item').removeClass('active');
    self.addClass('active');
  });

  function getFormData() {
    return {
      jiraProjectId: dialogEl.find('input[name="jiraProjectId"]').val(),
      testId: dialogEl.find('input[name="testId"]').val(),
      urlId: null
    };
  }
})(AJS || {}, AJS.$ || jQuery);
