/**
 * Created by Ludvig on 2016-04-21.
 */
function genScripts(){
    var script1 = document.createElement("script");
    script1.setAttribute("src","js/classie.js");
    script1.setAttribute("id","script1");
    document.body.appendChild(script1);
    var script2 = document.createElement("script");
    script2.setAttribute("id","script2");
    script2.setAttribute("src","js/modalEffects.js");
    document.body.appendChild(script2);
}

function reloadScripts() {
    console.log(2);
    var script1 = document.getElementById("script1");
    var script2 = document.getElementById("script2");
    document.body.removeChild(script1);
    document.body.removeChild(script2);
    genScripts();
}

// tutorial1.js
var SideNav = React.createClass({
    getInitialState : function() {
        return { showMe : false };
    },
    componentDidMount: function(){

        genScripts();
    },

    handleClick1: function (bindValue) {

        if(bindValue == "hwTest"){
            console.log("wse")
            ReactDOM.render(<HardwareTest />, document.getElementById('modal-container'));

            reloadScripts();
            var container = document.getElementById("tst");
            if(container.childNodes.length > 0){
                container.removeChild(container.childNodes.item(0));
            }
        }
        else {
            ReactDOM.render(bindValue, document.getElementById('content'));
        }


    },

    render: function() {
        return (
            <ul >
                <li>
                    <a onClick={this.handleClick1.bind(this,<ContainerBox />)} href="#">
                        item1
                    </a>
                </li>
                <li>
                    <a onClick={this.handleClick1.bind(this,<FormBox />)} href="#">item2</a>
                </li>
                <li>
                    <a onClick={this.handleClick1.bind(this,<StudentList />)} href="#">item3</a>
                </li>
                <li>
                    <a onClick={this.handleClick1.bind(this,"hwTest")} href="#" className="md-trigger md-setperspective" data-modal="modal-18">item4</a>
                </li>
                
            </ul>


        );
    }
});
ReactDOM.render(
    <SideNav />,
    document.getElementById('menu-container')
);