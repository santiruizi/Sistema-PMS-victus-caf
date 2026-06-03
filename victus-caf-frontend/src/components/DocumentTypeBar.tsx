interface DocumentTypeBarProps {
  value: string;
  onChange: (value: string) => void;
  disabled?: boolean;
}

const TIPOS = [
  { code: 'CC', label: 'CC' },
  { code: 'TI', label: 'TI' },
  { code: 'CE', label: 'CE' },
] as const;

export default function DocumentTypeBar({ value, onChange, disabled }: DocumentTypeBarProps) {
  return (
    <div className="flex rounded-md border border-gray-300 overflow-hidden">
      {TIPOS.map((tipo) => (
        <button
          key={tipo.code}
          type="button"
          disabled={disabled}
          onClick={() => onChange(tipo.code)}
          className={`flex-1 py-2 text-sm font-medium transition-colors ${
            value === tipo.code
              ? 'bg-blue-600 text-white'
              : 'bg-white text-gray-700 hover:bg-gray-50'
          } ${disabled ? 'opacity-50 cursor-not-allowed' : ''}`}
        >
          {tipo.label}
        </button>
      ))}
    </div>
  );
}
