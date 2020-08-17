mkdir app\src\main\res\values-zh-rMO
mkdir app\src\main\res\values-zh-rHK
mkdir app\src\main\res\values-zh-rTW
mkdir controller\src\main\res\values-zh-rMO
mkdir controller\src\main\res\values-zh-rHK
mkdir controller\src\main\res\values-zh-rTW
python -m opencc -c s2t -i app\src\main\res\values-zh\strings.xml -o app\src\main\res\values-zh-rMO\strings.xml
python -m opencc -c s2hk -i app\src\main\res\values-zh\strings.xml -o app\src\main\res\values-zh-rHK\strings.xml
python -m opencc -c s2twp -i app\src\main\res\values-zh\strings.xml -o app\src\main\res\values-zh-rTW\strings.xml
python -m opencc -c s2t -i controller\src\main\res\values-zh\strings.xml -o controller\src\main\res\values-zh-rMO\strings.xml
python -m opencc -c s2hk -i controller\src\main\res\values-zh\strings.xml -o controller\src\main\res\values-zh-rHK\strings.xml
python -m opencc -c s2twp -i controller\src\main\res\values-zh\strings.xml -o controller\src\main\res\values-zh-rTW\strings.xml