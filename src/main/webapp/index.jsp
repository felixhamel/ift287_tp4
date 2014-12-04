<!doctype html>
<html lang="en" ng-app="tp4">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Ligue de Baseball</title>
    <link rel="stylesheet" href="css/bootstrap/bootstrap.min.css" />
    <link rel="stylesheet" href="css/pace/pace.css" />
    <link rel="stylesheet" href="css/app.css" />
</head>
<body>

  <header class="container">
    <div class="row">
      <div class="col-md-12">
        <h2 style="float: left">Ligue de Baseball</h2>
        <a href="" class="btn btn-danger" style="margin-top: 20px; float: right">D&eacute;connexion</a>
        <div class="clearfix"></div>
      </div>
      <div class="col-md-12" style="margin-top: 20px;">
        <ul class="list-inline">
          <li><a href="/#/index" class="btn btn-primary">Index</a></li>
          <li><a href="/#/player-list" class="btn btn-primary">Administrer joueurs</a></li>
          <li><a href="/#/team-list" class="btn btn-primary">Administrer &eacute;quipes</a></li>
          <li><a href="/#/match-list" class="btn btn-primary">Administrer matchs</a></li>
          <li><a href="/#/official-list" class="btn btn-primary">Administrer arbitres</a></li>
        </ul>
      </div>
    </div>
  </header>

	<div ng-view style="margin-top: 25px">
	</div>

  <div class="container">
    <hr />
    Travail pratique 4, F&eacute;lix Hamel, Tommy, Erika, Alexandre Blais
  </div>
  <br /><br />

	<!-- JQuery ================================================================ -->
	<script src="js/jquery/jquery-2.0.3.js"></script>

	<!-- Bootstrap ============================================================= -->
	<script src="js/bootstrap/bootstrap.min.js"></script>

	<!-- AngularJS ============================================================= -->
	<!-- In production use:
	<script src="//ajax.googleapis.com/ajax/libs/angularjs/1.0.7/angular.min.js"></script>
	-->
	<script src="lib/angular/angular.js"></script>
	<script src="lib/angular/angular-resource.js"></script>

	<!-- AngularJS App Code ==================================================== -->
	<script src="js/app.js"></script>
	<script src="js/services.js"></script>
	<script src="js/controllers.js"></script>
	<script src="js/filters.js"></script>
	<script src="js/directives.js"></script>

  <!-- Pace -->
  <script src="js/pace/pace.min.js"></script>

</body>
</html>
