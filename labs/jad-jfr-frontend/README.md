# JAD JFR Frontend - Java Flight Recorder 

 React 18 + TypeScript + Vite  JFR (Java Flight Recorder) ，。

##  

### 
- ****:  JFR 、、
- ****: 
- ****:  17+ 
- ****: ，
- ****: 

### 
- **CPU **: CPU 、CPU 、
- ****: 、
- **I/O **: 、
- ****: 、、
- ****: 、
- ****: 、CPU 

### 
- ** UI**:  Ant Design 5.2 
- ****: 、、
- ****: 
- ****:  TypeScript 

##  

### 
- **React 18.2.0**: UI ， Hooks
- **TypeScript 5.4.5**: ，
- **Vite 5.2.8**: ，
- **Ant Design 5.13.7**: UI ，
- **React Router 6.22.3**: 
- **Axios 1.6.0**: HTTP ， API 

### 
- **@vitejs/plugin-react**: Vite React 
- **Less 4.3.0**: CSS 
- **ESBuild**: 

### 
- `react` + `react-dom` - React 
- `antd` + `@ant-design/icons` - UI 
- `react-router-dom` - 
- `axios` - HTTP 

##  

```
jad-jfr-frontend/
├── src/
│   ├── components/          # React 
│   │   ├── FileUpload/      # 
│   │   │   ├── FileUpload.tsx
│   │   │   └── index.tsx
│   │   ├── FileTable/       # 
│   │   │   ├── FileTable.tsx
│   │   │   └── index.tsx
│   │   └── FlameGraph/      # 
│   │       ├── FlameStats.tsx
│   │       └── ReactFlameGraphWrapper.tsx
│   ├── pages/               # 
│   │   ├── Home/            # 
│   │   │   ├── Home.tsx
│   │   │   └── index.tsx
│   │   └── Analysis/        # 
│   │       ├── Analysis.tsx
│   │       └── index.tsx
│   ├── layouts/             # 
│   │   └── BasicLayout.tsx
│   ├── services/            # API 
│   │   ├── api.ts           # API 
│   │   ├── fileService.ts   # 
│   │   ├── jfr.ts           # JFR 
│   │   └── jfrService.ts    # JFR 
│   ├── stores/              # 
│   │   └── FileContext.tsx  # 
│   ├── hooks/               #  Hooks
│   │   └── useWindowSize.ts
│   ├── utils/               # 
│   │   ├── color.ts         # 
│   │   ├── format.ts        # 
│   │   └── formatFlamegraph.ts # 
│   ├── App.tsx              # 
│   ├── main.tsx             # 
│   └── global.less          # 
├── public/                  # 
├── dist/                    # 
├── index.html               # HTML 
├── package.json             # 
├── vite.config.ts           # Vite 
├── tsconfig.json            # TypeScript 
└── tsconfig.node.json       # Node.js TypeScript 
```

##  

### 
- **Node.js 18+**:  Node.js 18 
- **npm 9+**: 
- ****:  ES2020 

### 1. 
```bash
cd jad/labs/jad-jfr-frontend
npm install
```

### 2.  API
 `http://localhost:8200`，：

```typescript
// vite.config.ts
export default defineConfig({
  server: {
    proxy: {
      '/api': {
        target: 'http://localhost:8200',
        changeOrigin: true,
        secure: false
      }
    }
  }
})
```

### 3. 
```bash
npm run dev
```

 `http://localhost:5173` 

### 4. 
```bash
# 
npm run build

# 
npm run preview
```

### 5. 
- : `http://localhost:5173`
- : `http://localhost:8200`
- 

##  

### 1.  JFR 
- ""
-  `.jfr` （ 1GB）
- 

### 2. 
-  JFR 
- 、
- 

### 3. 
- 
- ""
- （ CPU Time、Memory Allocation ）

### 4. 
- 
- 、、
- 
- 

##  

### 
```bash
# 
npm install

# 
npm run dev

# 
npx tsc --noEmit

# 
npm run build

# 
npm run preview
```

### 

#### Vite 
```typescript
// vite.config.ts
export default defineConfig({
  plugins: [react()],
  server: {
    port: 5173,
    proxy: {
      '/api': 'http://localhost:8200'
    }
  },
  build: {
    outDir: 'dist',
    rollupOptions: {
      output: {
        manualChunks: {
          vendor: ['react', 'react-dom'],
          antd: ['antd', '@ant-design/icons']
        }
      }
    }
  }
})
```

#### TypeScript 
```json
{
  "compilerOptions": {
    "target": "ES2020",
    "jsx": "react-jsx",
    "strict": true,
    "baseUrl": ".",
    "paths": {
      "@/*": ["src/*"]
    }
  }
}
```

### 
-  TypeScript 
-  + Hooks
-  Ant Design 
-  React 
-  ESLint 

### 

#### 1. 
```typescript
// src/pages/NewPage/NewPage.tsx
import React from 'react';

const NewPage: React.FC = () => {
  return <div>New Page</div>;
};

export default NewPage;
```

#### 2. 
```typescript
// src/components/NewComponent/NewComponent.tsx
import React from 'react';
import { Button } from 'antd';

interface NewComponentProps {
  title: string;
  onClick: () => void;
}

const NewComponent: React.FC<NewComponentProps> = ({ title, onClick }) => {
  return <Button onClick={onClick}>{title}</Button>;
};

export default NewComponent;
```

#### 3.  API 
```typescript
// src/services/newService.ts
import { api } from './api';

export const newService = {
  getData: () => api.get('/new-endpoint'),
  postData: (data: any) => api.post('/new-endpoint', data)
};
```

##  

：

- **[Java Mission Control (JMC)](https://github.com/openjdk/jmc)** - Oracle  Java 
- **[JProfiler](https://www.ej-technologies.com/products/jprofiler/overview.html)** -  Java 
- **[VisualVM](https://visualvm.github.io/)** -  Java 
- **[FlameGraph](https://github.com/brendangregg/FlameGraph)** - 
- **[Jifa](https://github.com/eclipse-jifa/jifa)** - Java 

##  

### 
- [React ](https://react.dev/)
- [TypeScript ](https://www.typescriptlang.org/)
- [Vite ](https://vitejs.dev/)
- [Ant Design ](https://ant.design/)

### 
- [Spring Boot ](https://spring.io/projects/spring-boot)
- [Java Mission Control ](https://github.com/openjdk/jmc)
- [JFR ](https://openjdk.org/projects/jdk/8/)




