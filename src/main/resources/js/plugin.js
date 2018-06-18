(function (AJS, $) {
  $(function () {

    // triggers the ajax driven pop-up dialog that is displayed when the user clicks
    // on the "Execute" button in the "ALEX" labeled drop-down menu in each test.
    // The pop-up will display if the test could be executed in ALEX.
    new AJS.FormPopup({
      id: 'add-watchers-dialog',
      trigger: '#afj-operations-execute-item'
    });
  });
}(AJS || {}, AJS.$ || jQuery));
