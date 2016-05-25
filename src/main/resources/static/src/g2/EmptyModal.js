/**
 * Created by Ludvig on 2016-05-25.
 */
var EmptyModal = React.createClass({
    render: function () {
        return (<div>
            <div className="md-close button primary-button SCButton">Close</div>
        </div>);
    }
})

window.clearModal = function () {
    ReactDOM.render(<EmptyModal />
        ,document.getElementById('modal-container'));
}