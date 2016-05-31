/**
 * modalEffects.js v1.0.0
 * http://tympanus.net/codrops/2013/06/25/nifty-modal-window-effects/
 *
 * Licensed under the MIT license.
 * http://www.opensource.org/licenses/mit-license.php
 * 
 * Copyright 2013, Codrops
 * http://www.codrops.com
 */
var ModalEffects = (function() {

	function init() {

		var overlay = document.querySelector( '.md-overlay' );

		[].slice.call( document.querySelectorAll( '.md-trigger' ) ).forEach( function( el, i ) {
			
			var modal = document.getElementsByClassName("md-modal")[0];
			var close = document.getElementsByClassName("md-close")[0];
			function removeModal( hasPerspective ) {
				classie.remove( modal, 'md-show' );

				if( hasPerspective ) {
					classie.remove( $("html")[0], 'md-perspective' );
				}
			}

			function removeModalHandler() {
				removeModal( classie.has( el, 'md-setperspective' ) ); 
			}

			el.addEventListener( 'click', function( ev ) {
				classie.add( modal, 'md-show' );
				overlay.removeEventListener( 'click', removeModalHandler );
				overlay.addEventListener( 'click', removeModalHandler );
                console.log("open");
				if( classie.has( el, 'md-setperspective' ) ) {
					setTimeout( function() {
						classie.add( $("html")[0], 'md-perspective' );
					}, 25 );
				}
			});


			close.addEventListener( 'click', function( ev ) {
				ev.stopPropagation();
				removeModalHandler();

				window.clearModal();
			});



		} );

	}

	init();

})();

