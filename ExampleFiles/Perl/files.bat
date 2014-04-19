FOR /L %%A IN (000,1,100) DO (
  ECHO %%A
	echo. >NUL 2>prog%%A.txt
)