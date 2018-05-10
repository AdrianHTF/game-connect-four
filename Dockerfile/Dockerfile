# Base image source: https://github.com/hseeberger/scala-sbt
FROM hseeberger/scala-sbt

# Clone connect four game repository
RUN git clone https://github.com/Schmork/game-connect-four.git /soar
RUN (cd /soar/ && sbt sbtVersion)

# Define working directory
WORKDIR /soar/

# Start the game
CMD (cd /soar/ && sbt run)