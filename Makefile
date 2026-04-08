build:
	clj -Spom
	clj -A:jar scryfall-clj.jar

deploy:
	clj -X:deploy
