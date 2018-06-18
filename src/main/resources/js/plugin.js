jQuery(function () {

  // triggers the ajax driven pop-up dialog that is displayed when the user clicks
  // on the "Execute" button in the "ALEX" labeled drop-down menu in each test.
  // The pop-up will display if the test could be executed in ALEX.
  new AJS.FormPopup({
    id: 'add-watchers-dialog',
    trigger: '#my-item-link'
  });

  // remove the dummy web item from the dom so that only the
  // dropdown button is displayed
  $('#my-item-link-dummy').parent().remove();

  // intercept every http request made to the Zephyr plugin and check if step related
  // actions are made. This is necessary because the Zephyr plugin does not publish
  // test related events natively, so we cannot listen for them server side.
  $(document).ajaxSend(function (event, jqxhr, settings) {

    /**
     * The URI of the REST API resource of the plugin.
     * @type {string}
     */
    var API_URL = AJS.contextPath() + '/rest/alex/1.0/events/steps';

    /**
     * Events that are logged.
     * @type {{STEP_CREATED: string, STEP_UPDATED: string, STEP_DELETED: string, STEP_CLONED: string, STEP_MOVED: string}}
     */
    var events = {
      'STEP_CREATED': 'STEP_CREATED',
      'STEP_UPDATED': 'STEP_UPDATED',
      'STEP_DELETED': 'STEP_DELETED',
      'STEP_CLONED': 'STEP_CLONED',
      'STEP_MOVED': 'STEP_MOVED'
    };

    var patterns = {
      'BASE_URL': '/rest/zephyr',
      'STEP_CREATED': /^\/rest\/zephyr\/latest\/teststep\/([0-9]+)$/,
      'STEP_UPDATED_DELETED': /^\/rest\/zephyr\/latest\/teststep\/([0-9]+)\/([0-9]+)(\?id=[0-9]+)?$/,
      'STEP_CLONED': /^\/rest\/zephyr\/latest\/teststep\/([0-9]+)\/clone\/([0-9]+)$/,
      'STEP_MOVED': /^\/rest\/zephyr\/latest\/teststep\/([0-9]+)\/([0-9]+)\/move$/
    };

    /**
     * Sends event data of the test step to the rest interface of the plugin.
     *
     * @param {string} type The event type.
     * @param {string} issueId The id of the issue.
     * @param {string|null} stepId The id of the step, if any.
     * @param {string|null} data The data that is send, if any.
     */
    function sendData(type, issueId, stepId, data) {
      var postData = JSON.stringify({
        type: type,
        issueId: parseInt(issueId),
        stepId: parseInt(stepId),
        data: data
      });

      $.ajax({
        url: API_URL,
        type: 'POST',
        contentType: 'application/json',
        data: postData,
        processData: false
      }).done(console.log).error(console.error);
    }

    // the URL of the request
    var url = settings.url;
    if (url.startsWith(patterns.BASE_URL)) {

      // is a new step created?
      var matches = url.match(patterns.STEP_CREATED);
      if (matches != null && settings.type === 'POST') {
        sendData(events.STEP_CREATED, matches[1], null, settings.data);
        return;
      }

      // is a step updated or deleted?
      matches = url.match(patterns.STEP_UPDATED_DELETED);
      if (matches != null) {
        if (settings.type === 'PUT') {
          sendData(events.STEP_UPDATED, matches[1], matches[2], settings.data);
        } else if (settings.type === 'DELETE') {
          sendData(events.STEP_DELETED, matches[1], matches[2], null);
        }
        return;
      }

      // is a step moved to another position?
      matches = url.match(patterns.STEP_MOVED);
      if (matches != null && settings.type === 'POST') {
        sendData(events.STEP_MOVED, matches[1], matches[2], settings.data);
        return;
      }

      // is a step cloned?
      matches = url.match(patterns.STEP_CLONED);
      if (matches != null && settings.type === 'POST') {
        sendData(events.STEP_CLONED, matches[1], matches[2], settings.data);
      }
    }
  });
});
