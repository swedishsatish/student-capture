/* student */

QUnit.test( "hello test", function( assert ) {
  assert.ok( 1 == "1", "Passed!" );
});

QUnit.test( "startStream", function( assert ) {
  assert.ok( startStream("video#gum"), true);
});


QUnit.test( "stopRecordningTRUE", function( assert ) {
    var done = assert.async();
    startStream("video#gum")
    setTimeout(function(){
        startRecording(getStream());
        assert.ok(stopRecording());
        done();
    }, 500);


});


QUnit.test( "stopRecordningFALSE", function( assert ) {
    var done = assert.async();
    startStream("video#gum")
    setTimeout(function(){
        //startRecording(getStream());
        assert.notOk(stopRecording());
        done();
    }, 500);


});


QUnit.test( "startRecordningTRUE", function( assert ) {
    var done = assert.async();
    startStream("video#gum")
    setTimeout(function(){
        assert.ok(startRecording(getStream(), true))
        done();

    }, 500);
});

QUnit.test( "startRecordningFALSE", function( assert ) {
    var done = assert.async();
    startStream("video#gum")
    setTimeout(function(){
        assert.notOk(startRecording([]))
        done();
    }, 500);
});



QUnit.test( "recordFeedbackTRUE", function( assert ) {
    recordFeedback(true);
    assert.ok(document.getElementById("recordPar").hasChildNodes());
});

QUnit.test( "recordFeedbackFALSE", function( assert ) {
    recordFeedback(true);
    recordFeedback(false);

    var tesT = document.getElementById("recordPar");

    assert.notOk(tesT.hasChildNodes());
});

/* teacher main */


QUnit.test( "disabledButton", function( assert ) {
    assert.ok(document.getElementById("post").disabled);
    toggle();
    assert.ok(document.getElementById("post").disabled);
    toggle();
    assert.notOk(document.getElementById("post").disabled);

});
