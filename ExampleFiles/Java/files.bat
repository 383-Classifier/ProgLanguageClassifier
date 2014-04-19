FOR /L %%A IN (10,1,29) DO (
  ECHO %%A
	echo. >NUL 2>prog%%A.txt
)