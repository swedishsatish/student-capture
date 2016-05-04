/**
 * Created by Ludvig on 2016-04-21.
 */




var PageContent1 = React.createClass({

    render : function() {
        if(this.props.showing) {
            return (
                <div class="commentForm one column">
                    <p>Task1</p>
                </div>
            );
        } else {
            return (
                <div class="commentForm one column">
                    <p>Task2</p>
                </div>
            );
        }
    }
});




var ToggleButton = React.createClass({


    render: function() {

        if(this.props.showing) {
            return (
                <button class="button-primary" onClick={this.props.fnClick}>Toggle1
                </button>
            );
        } else {
            return (
                <button class="button-primary" onClick={this.props.fnClick}>Toggle2
                </button>
            );
        }
    }
});

// tutorial1.js
var ContainerBox = React.createClass({
    getInitialState : function() {
        return { showMe : false };
    },

    handleClick: function (event) {

        this.setState({showMe:!this.state.showMe});

    },
    render: function() {
        return (
            <div class="row">
                <div class="two columns">
                    <ToggleButton fnClick={this.handleClick} showing={this.state.showMe}/>
                </div>
                <PageContent1 showing={this.state.showMe} />
            </div>


        );
    }
});

window.ContainerBox = ContainerBox;