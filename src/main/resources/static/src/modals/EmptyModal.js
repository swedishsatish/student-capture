/**
 * @author Ludvig Boström, c13lbm
 */

/**
 * empty modal used to clear modals on close.
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