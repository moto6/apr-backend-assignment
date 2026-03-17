BASE_URL=http://localhost:8080

many-request:
	@echo "[친구 수 제한: 최대 10,000명까지만 친구 수락이 가능함] 항목 검증 시작"
	@for i in $$(seq 1 20); do \
		echo "--- Try $$i ---"; \
		res=$$(curl -s -w "\n%{http_code}" -X POST $(BASE_URL)/api/v1/friend/request \
			-H "X-User-Id: 1" \
			-H "Content-Type: application/json" \
			-d '{"toAccountId": 2}'); \
		echo "$$res"; \
		code=$$(echo "$$res" | tail -n 1); \
		if [ "$$code" -ne 200 ]; then \
			echo "❌ Stopped: Status $$code"; \
			break; \
		fi; \
	done
	@echo "[친구 수 제한: 최대 10,000명까지만 친구 수락이 가능함] 항목 검증 완료"


test:
	@echo "[4.필수 구현 API 목록] 항목 검증 시작"
	@echo "Step 1: Execute API integration tests"
	@mkdir -p asset
	@bash asset/test.sh
	@echo "[4.필수 구현 API 목록] 항목 검증 완료"


reset:
	@echo "Step 1: Stopping and removing existing container"
	@docker rm -f apr-server 2>/dev/null || true
	@echo "Step 2: Restarting setup process"
	@$(MAKE) setup


setup:
	@echo "Step 1: Build executable jar"
	@./gradlew bootJar > /dev/null
	@echo "Step 2: Build docker image"
	@docker build -t apr-backend .
	@echo "Step 3: Remove existing container"
	@docker rm -f apr-server 2>/dev/null || true
	@echo "Step 4: Run docker container"
	@docker run -d -p 8080:8080 --name apr-server apr-backend
	@echo "Done"
	@echo "API Docs : http://localhost:8080/swagger-ui/index.html"


clean:
	@echo "Step 1: Clean build artifacts"
	@./gradlew clean > /dev/null
	@echo "Step 2: Remove container and image"
	@docker rm -f apr-server 2>/dev/null || true
	@docker rmi -f apr-backend 2>/dev/null || true
	@echo "Step 3: Clear docker build cache"
	@docker builder prune -f > /dev/null
	@echo "Done"